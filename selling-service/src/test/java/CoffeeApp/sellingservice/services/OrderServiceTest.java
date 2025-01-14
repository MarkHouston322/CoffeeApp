package CoffeeApp.sellingservice.services;

import CoffeeApp.sellingservice.dto.GoodInOrderDto;
import CoffeeApp.sellingservice.dto.messages.GoodMessage;
import CoffeeApp.sellingservice.dto.messages.ProceedOrderMessage;
import CoffeeApp.sellingservice.dto.orderDto.AddOrderDto;
import CoffeeApp.sellingservice.dto.orderDto.OrderDto;
import CoffeeApp.sellingservice.dto.orderDto.OrderResponse;
import CoffeeApp.sellingservice.dto.paymentMethodDto.PaymentMethodDto;
import CoffeeApp.sellingservice.exceptions.ResourceNotFoundException;
import CoffeeApp.sellingservice.exceptions.SessionIsClosedException;
import CoffeeApp.sellingservice.models.GoodInOrder;
import CoffeeApp.sellingservice.models.Order;
import CoffeeApp.sellingservice.models.PaymentMethod;
import CoffeeApp.sellingservice.projections.GoodProjection;
import CoffeeApp.sellingservice.projections.GoodProjectionImpl;
import CoffeeApp.sellingservice.repositories.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private GoodInOrderService goodInOrderService;

    @Mock
    private PaymentMethodService paymentMethodService;

    @Mock
    private KafkaTemplate<String, ProceedOrderMessage> orderKafkaTemplate;

    @Mock
    private StreamBridge streamBridge;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderService orderService;

    @Test
    void shouldReturnOrderDtoById() {
        // given
        int id = 1;
        LocalDateTime localDateTime = LocalDateTime.now();
        Order order = new Order();
        order.setOrderDate(localDateTime);
        order.setId(id);
        when(orderRepository.findById(id)).thenReturn(Optional.of(order));
        // when
        OrderDto result = orderService.findById(id);
        // then
        assertThat(result).isNotNull();
        assertThat(result.getOrderDate()).isEqualTo(dateFormatter(order.getOrderDate()));
        verify(orderRepository).findById(id);
    }

    @Test
    void shouldNotReturnOrderDtoByIdAndThrowException() {
        // given
        int id = 1;
        when(orderRepository.findById(id)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> orderService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order", "id", Integer.toString(id));
        verify(orderRepository).findById(id);
    }

    @Test
    void shouldReturnAllOrdersDto() {
        // given
        LocalDateTime dateTime1 = LocalDateTime.now();
        LocalDateTime dateTime2 = LocalDateTime.now();
        Order order1 = new Order();
        order1.setOrderDate(dateTime1);
        Order order2 = new Order();
        order2.setOrderDate(dateTime2);
        List<Order> orders = Arrays.asList(order1, order2);
        OrderDto orderDto1 = new OrderDto();
        orderDto1.setOrderDate(dateFormatter(dateTime1));
        OrderDto orderDto2 = new OrderDto();
        orderDto2.setOrderDate(dateFormatter(dateTime2));
        when(orderRepository.findAll()).thenReturn(orders);
        when(modelMapper.map(order1,OrderDto.class)).thenReturn(orderDto1);
        when(modelMapper.map(order2,OrderDto.class)).thenReturn(orderDto2);
        // when
        OrderResponse response = orderService.findAll();
        // then
        assertThat(response.getOrders()).containsExactly(orderDto1, orderDto2);
        verify(orderRepository).findAll();
    }

    @Test
    void shouldNotReturnAllOrdersDto(){
        // given
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());
        // when
        OrderResponse response = orderService.findAll();
        // then
        assertThat(response.getOrders()).isEmpty();
        verify(orderRepository).findAll();
    }

    @Test
    void shouldReturnGoodsInOrderWhenOrderExists() {
        // given
        Integer orderId = 1;
        Order order = new Order();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        List<GoodProjection> projections = List.of(
                new GoodProjectionImpl("Coffee", 2),
                new GoodProjectionImpl("Tea", 1)
        );
        when(orderRepository.findGoodsByOrderId(orderId)).thenReturn(projections);
        // when
        List<GoodInOrderDto> goods = orderService.getGoodsInOrder(orderId);
        // then
        assertThat(goods).hasSize(2);
        assertThat(goods).extracting(GoodInOrderDto::getGoodName).containsExactly("Coffee", "Tea");
        assertThat(goods).extracting(GoodInOrderDto::getQuantity).containsExactly(2, 1);
        verify(orderRepository).findById(orderId);
        verify(orderRepository).findGoodsByOrderId(orderId);
    }

    @Test
    void shouldAddOrderSuccessfully() throws JsonProcessingException, com.fasterxml.jackson.core.JsonProcessingException {
        // given
        AddOrderDto addOrderDto = new AddOrderDto();
        Map<String, String> goods = Map.of("coffee", "2", "tea", "1");
        String customerId = "12345";

        String sessionId = "1";
        String employeeName = "John Doe";
        String coffeeJson = "{\"name\": \"coffee\", \"price\": 100, \"type\": \"drink\"}";
        String teaJson = "{\"name\": \"tea\", \"price\": 50, \"type\": \"drink\"}";

        GoodMessage coffeeMessage = new GoodMessage("coffee", 100, "drink");
        GoodMessage teaMessage = new GoodMessage("tea", 50, "drink");

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("Session is closed")).thenReturn("false");
        when(valueOperations.get("Session id")).thenReturn(sessionId);
        when(valueOperations.get("Employee name")).thenReturn(employeeName);
        when(valueOperations.get("coffee")).thenReturn(coffeeJson);
        when(valueOperations.get("tea")).thenReturn(teaJson);

        when(objectMapper.readValue(coffeeJson, GoodMessage.class)).thenReturn(coffeeMessage);
        when(objectMapper.readValue(teaJson, GoodMessage.class)).thenReturn(teaMessage);

        Order mockOrder = new Order();
        PaymentMethod paymentMethod = new PaymentMethod("Cash");
        mockOrder.setPaymentMethod(paymentMethod);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        when(modelMapper.map(addOrderDto,Order.class)).thenReturn(mockOrder);

        PaymentMethodDto paymentMethodDto = new PaymentMethodDto( "Cash");
        when(paymentMethodService.findById(anyInt())).thenReturn(paymentMethodDto);

        // when
        orderService.addOrder(addOrderDto, goods, customerId);

        // then
        verify(redisTemplate.opsForValue(), times(5)).get(anyString());
        verify(objectMapper).readValue(coffeeJson, GoodMessage.class);
        verify(objectMapper).readValue(teaJson, GoodMessage.class);
        verify(orderRepository).save(any(Order.class));
        verify(streamBridge, times(4)).send(anyString(), any());
        verify(orderKafkaTemplate).send(eq("orders"), any(ProceedOrderMessage.class));
        verify(goodInOrderService, times(2)).addDrinkInOrder(any(GoodInOrder.class));
    }

    @Test
    void shouldThrowExceptionWhenSessionIsClosed() {
        // given
        AddOrderDto addOrderDto = new AddOrderDto();
        Map<String, String> goods = Map.of("coffee", "2");
        String customerId = "12345";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForValue().get("Session is closed")).thenReturn("true");
        // when & then
        assertThatThrownBy(() -> orderService.addOrder(addOrderDto, goods, customerId))
                .isInstanceOf(SessionIsClosedException.class)
                .hasMessageContaining("There is no opened session");
        verify(redisTemplate, times(3)).opsForValue().get("Session is closed");
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(streamBridge);
    }

    @Test
    void shouldThrowExceptionWhenSessionNotExists() {
        // given
        AddOrderDto addOrderDto = new AddOrderDto();
        Map<String, String> goods = Map.of("coffee", "2");
        String customerId = "12345";
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(redisTemplate.opsForValue().get("Session is closed")).thenReturn(null);
        // when & then
        assertThatThrownBy(() -> orderService.addOrder(addOrderDto, goods, customerId))
                .isInstanceOf(SessionIsClosedException.class)
                .hasMessageContaining("There is no opened session");
        verify(redisTemplate.opsForValue()).get("Session is closed");
        verifyNoInteractions(orderRepository);
        verifyNoInteractions(streamBridge);
    }

    private static String dateFormatter(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return localDateTime.format(formatter);
    }
}