package CoffeeApp.sellingservice.services;

import CoffeeApp.sellingservice.dto.*;
import CoffeeApp.sellingservice.dto.GoodInOrderDto;
import CoffeeApp.sellingservice.dto.messages.GoodMessage;
import CoffeeApp.sellingservice.dto.messages.*;
import CoffeeApp.sellingservice.dto.orderDto.AddOrderDto;
import CoffeeApp.sellingservice.dto.orderDto.OrderDto;
import CoffeeApp.sellingservice.dto.messages.OrderMessage;
import CoffeeApp.sellingservice.dto.orderDto.OrderResponse;
import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodDto;
import CoffeeApp.sellingservice.exceptions.ResourceNotFoundException;
import CoffeeApp.sellingservice.exceptions.SessionIsClosedException;
import CoffeeApp.sellingservice.mappers.OrderMapper;
import CoffeeApp.sellingservice.models.GoodInOrder;
import CoffeeApp.sellingservice.models.Order;
import CoffeeApp.sellingservice.projections.GoodProjection;
import CoffeeApp.sellingservice.repositories.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class OrderService {


    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private final OrderRepository orderRepository;
    private final GoodInOrderService goodInOrderService;
    private final PaymentMethodService paymentMethodService;
    private final ModelMapper modelMapper;
    private final OrderMapper orderMapper;
    private final StreamBridge streamBridge;
    private final KafkaTemplate<String, ProceedOrderMessage> orderKafkaTemplate;
    private final KafkaTemplate<String, SoldDrinkMessage> soldDrinkKafkaTemplate;

    public OrderDto findById(Integer id) {
        Order order = checkIfExists(id);
        return convertToOrderDto(order);
    }

    public OrderResponse findAll() {
        return new OrderResponse(orderRepository.findAll().stream().map(this::convertToOrderDto)
                .collect(Collectors.toList()));
    }

    public List<GoodInOrderDto> getGoodsInOrder(Integer orderId) {
        checkIfExists(orderId);
        List<GoodProjection> projections = orderRepository.findGoodsByOrderId(orderId);
        return projections.stream()
                .map(projection -> new GoodInOrderDto(projection.goodName(), projection.goodQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addOrder(AddOrderDto addOrderDto, Map<String, String> goods, String customerId) throws JsonProcessingException {
        if (redisTemplate.opsForValue().get("Session is closed") == null) {
            throw new SessionIsClosedException("There is no opened session. Open a new one, to make order");
        } else if (Boolean.parseBoolean(redisTemplate.opsForValue().get("Session is closed"))) {
            throw new SessionIsClosedException("There is no opened session. Open a new one, to make order");
        }
        Order orderToAdd = convertToOrder(addOrderDto);
        calculateAndSetTotalPrice(orderToAdd, goods);
        calculateAndSetDiscountAndTotalWithDesc(orderToAdd, customerId);
        addCustomerInOrderIfExists(orderToAdd, customerId);
        orderToAdd.setOrderDate(LocalDateTime.now());
        orderToAdd.setSessionId(Integer.parseInt(Objects.requireNonNull(redisTemplate.opsForValue().get("Session id"))));
        orderToAdd.setEmployeeName(Objects.requireNonNull(redisTemplate.opsForValue().get("Employee name")));
        orderRepository.save(orderToAdd);
        saveGoodInOrder(orderToAdd, goods);
        sendSoldDrink(orderToAdd, goods);
        sendCustomerPurchaseIfExists(orderToAdd);
        sendPayment(orderToAdd);
        sendRevenue(orderToAdd);
        sendOrderMessage(orderToAdd, orderKafkaTemplate);
    }


    private void saveGoodInOrder(Order order, Map<String, String> goods) throws JsonProcessingException {
        for (Map.Entry<String, String> entry : goods.entrySet()) {
            String json = redisTemplate.opsForValue().get(entry.getKey());
            GoodMessage goodMessage = objectMapper.readValue(json, GoodMessage.class);
            int quantity = Integer.parseInt(entry.getValue());
            GoodInOrder goodInOrder = new GoodInOrder(order, quantity, goodMessage.getName(), goodMessage.getPrice(), false);
            goodInOrderService.addDrinkInOrder(goodInOrder);
            sendSoldDrinkMessage(goodInOrder, soldDrinkKafkaTemplate);

        }
    }

    private void calculateAndSetTotalPrice(Order order, Map<String, String> goods) throws JsonProcessingException {
        int price = 0;
        for (Map.Entry<String, String> entry : goods.entrySet()) {
            String json = redisTemplate.opsForValue().get(entry.getKey());
            GoodMessage goodMessage = objectMapper.readValue(json, GoodMessage.class);
            int quantity = Integer.parseInt(entry.getValue());
            int goodPrice = goodMessage.getPrice() * quantity;
            price += goodPrice;
        }
        order.setTotal(price);
    }

    private void calculateAndSetDiscountAndTotalWithDesc(Order order, String customerId) {
        CustomerInOrderDto customer = getCustomer(customerId);
        if (customer != null) {
            order.setDiscount((int) Math.ceil(order.getTotal() * customer.getDiscountPercentage()));
            order.setTotalWithDsc(order.getTotal() - order.getDiscount());
        } else {
            order.setDiscount(0);
            order.setTotalWithDsc(order.getTotal());
        }
    }

    private void addCustomerInOrderIfExists(Order order, String customerId) {
        CustomerInOrderDto customer = getCustomer(customerId);
        if (customer != null) {
            order.setCustomerId(customer.getId());
            order.setCustomerPurchaseConfirmation(false);
        } else {
            order.setCustomerId(null);
            order.setCustomerPurchaseConfirmation(null);
        }
    }

    private void sendCustomerPurchaseIfExists(Order order) {
        if (order.getCustomerId() != null) {
            CustomerMessage customerMessage = new CustomerMessage(order.getId(), order.getCustomerId(), order.getTotal());
            streamBridge.send("customerPurchase-out-0", customerMessage);
        }
    }


    public CustomerInOrderDto getCustomer(String key) {
        String json = redisTemplate.opsForValue().get(key);
        try {
            return objectMapper.readValue(json, CustomerInOrderDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveGoodCache(GoodMessage goodMessage) {
        try {
            redisTemplate.opsForValue().set(goodMessage.getName(), objectMapper.writeValueAsString(goodMessage));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveSessionCache(SessionMessage sessionMessage) {
        try {
            redisTemplate.opsForValue().set("Session is closed", objectMapper.writeValueAsString(sessionMessage.getSessionIsClosed()));
            redisTemplate.opsForValue().set("Session id", objectMapper.writeValueAsString(sessionMessage.getId()));
            redisTemplate.opsForValue().set("Employee name", sessionMessage.getEmployeeUsername());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void saveCustomerCache(CustomerInOrderDto customerInOrderDto) {
        try {
            redisTemplate.opsForValue().set(objectMapper.writeValueAsString(customerInOrderDto.getId()), objectMapper.writeValueAsString(customerInOrderDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateWriteOffConfirmation(Integer orderId) {
        if (orderId != null) {
            orderRepository.findById(orderId).orElseThrow(
                    () -> new ResourceNotFoundException("Order", "Order ID", orderId.toString())
            );
            List<GoodInOrder> drinksInOrder = goodInOrderService.findByOrderId(orderId);
            for (GoodInOrder drink : drinksInOrder) {
                drink.setSoldConfirmation(true);
                goodInOrderService.addDrinkInOrder(drink);
            }
        }
    }

    public void updateCustomerPurchaseConfirmation(Integer orderId) {
        if (orderId != null) {
            Order order = orderRepository.findById(orderId).orElseThrow(
                    () -> new ResourceNotFoundException("Order", "Order ID", orderId.toString())
            );
            order.setCustomerPurchaseConfirmation(true);
        }
    }

    private void sendSoldDrink(Order order, Map<String, String> drinks) throws JsonProcessingException {
        for (Map.Entry<String, String> entry : drinks.entrySet()) {
            String json = redisTemplate.opsForValue().get(entry.getKey());
            GoodMessage goodMessage = objectMapper.readValue(json, GoodMessage.class);
            OrderMessage orderMessage = new OrderMessage(order.getId(), entry.getKey(), Integer.parseInt(entry.getValue()), goodMessage.getType());
            streamBridge.send("sellGood-out-0", orderMessage);
        }
    }

    private void sendPayment(Order order) {
        PaymentMethodDto paymentMethodDto = paymentMethodService.findById(order.getPaymentMethod().getId());
        String paymentMethodName = paymentMethodDto.getName();
        PaymentMessage paymentMessage = new PaymentMessage(paymentMethodName, order.getTotal());
        streamBridge.send("sendPayment-out-0", paymentMessage);
    }

    private void sendRevenue(Order order) {
        EmployeeMessage employeeMessage = new EmployeeMessage(order.getEmployeeName(), order.getTotal());
        streamBridge.send("sendRevenue-out-0", employeeMessage);
    }

    private Order checkIfExists(int id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "id", Integer.toString(id))
        );
    }

    private void sendOrderMessage(Order order, KafkaTemplate<String, ProceedOrderMessage> kafkaTemplate){
        ProceedOrderMessage proceedOrderMessage = new ProceedOrderMessage(LocalDateTime.now(), order.getTotal(), order.getDiscount(), order.getTotalWithDsc());
        kafkaTemplate.send("orders", proceedOrderMessage);
    }

    private void sendSoldDrinkMessage(GoodInOrder goodInOrder, KafkaTemplate<String, SoldDrinkMessage> kafkaTemplate){
        SoldDrinkMessage soldDrinkMessage = new SoldDrinkMessage(goodInOrder.getGoodName(), goodInOrder.getQuantity(), LocalDateTime.now());
        kafkaTemplate.send("drinks",soldDrinkMessage);
    }

    private OrderDto convertToOrderDto(Order order) {
        return OrderMapper.mapToOrderDto(order);
    }

    private Order convertToOrder(AddOrderDto addOrderDto) {
        return modelMapper.map(addOrderDto, Order.class);
    }

}
