package CoffeeApp.sellingservice.repositories;

import CoffeeApp.sellingservice.models.GoodInOrder;
import CoffeeApp.sellingservice.models.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class GoodInOrderRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("good_in_order.sql");

    @Autowired
    private GoodInOrderRepository goodInOrderRepository;

    private final Order order = new Order();

    @AfterEach
    void clearUp(){
        goodInOrderRepository.deleteAll();
    }

    @Test
    void shouldReturnGoodsByOrderId(){
        order.setId(1);
        List<GoodInOrder> goods = new ArrayList<>(Arrays.asList(
                new GoodInOrder(order,1,"water",100,true),
                new GoodInOrder(order,1,"milk",100,true)
        ));
        goodInOrderRepository.saveAll(goods);
        List<GoodInOrder> goodsInOrder = goodInOrderRepository.findByOrder_Id(order.getId());
        assertThat(goodsInOrder).isNotEmpty();
    }

    @Test
    void shouldNotReturnGoodsByOrderId() {
        List<GoodInOrder> goodInOrders = goodInOrderRepository.findByOrder_Id(order.getId());
        assertThat(goodInOrders).isEmpty();
    }
}