package CoffeeApp.sellingservice.repositories;

import CoffeeApp.sellingservice.models.GoodInOrder;
import CoffeeApp.sellingservice.models.Order;
import CoffeeApp.sellingservice.projections.GoodProjection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
class OrderRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript("order.sql");

    @Autowired
    private GoodInOrderRepository goodInOrderRepository;

    @AfterEach
    void clearUp(){
        goodInOrderRepository.deleteAll();
    }

    @Test
    void shouldFindGoodsByOrderId(){
        Order order = new Order();
        order.setId(1);
        List<GoodInOrder> goods = new ArrayList<>(Arrays.asList(
                new GoodInOrder(order,1,"Water",100,true),
                new GoodInOrder(order,1,"Milk",100,true)
        ));
        goodInOrderRepository.saveAll(goods);
        List<GoodInOrder> goodInOrders = goodInOrderRepository.findByOrder_Id(1);
        assertThat(goodInOrders).isNotEmpty();
    }

    @Test
    void shouldNotFindGoodsByOrderId(){
        List<GoodInOrder> goodInOrders = goodInOrderRepository.findByOrder_Id(2);
        assertThat(goodInOrders).isEmpty();
    }
}