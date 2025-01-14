package CoffeeApp.sellingservice.services;

import CoffeeApp.sellingservice.models.GoodInOrder;
import CoffeeApp.sellingservice.models.Order;
import CoffeeApp.sellingservice.repositories.GoodInOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoodInOrderServiceTest {

    @Mock
    private GoodInOrderRepository goodInOrderRepository;

    @InjectMocks
    private GoodInOrderService goodInOrderService;

    @Test
    void shouldReturnGoodsInOrderById(){
        // given
        Order order = new Order();
        GoodInOrder good1 = new GoodInOrder();
        good1.setOrder(order);
        GoodInOrder good2 = new GoodInOrder();
        good2.setOrder(order);
        List<GoodInOrder> goods = Arrays.asList(good1,good2);
        when(goodInOrderRepository.findByOrder_Id(Mockito.anyInt())).thenReturn(goods);
        // when
        List<GoodInOrder> result = goodInOrderService.findByOrderId(Mockito.anyInt());
        // then
        assertThat(result).containsExactly(good1,good2);
        verify(goodInOrderRepository).findByOrder_Id(Mockito.anyInt());
    }

    @Test
    void shouldNotReturnGoodsInOrderById() {
        // when
        when(goodInOrderRepository.findByOrder_Id(Mockito.anyInt())).thenReturn(Collections.emptyList());
        // then
        List<GoodInOrder> result = goodInOrderService.findByOrderId(Mockito.anyInt());
        // then
        assertThat(result).isEmpty();
        verify(goodInOrderRepository).findByOrder_Id(Mockito.anyInt());
    }

    @Test
    void shouldAddGoodInOrder(){
        // given
        Order order = new Order();
        GoodInOrder good = new GoodInOrder(order,1,"water",100,true);
        // when
        goodInOrderRepository.save(good);
        // then
        ArgumentCaptor<GoodInOrder> goodCaptor = ArgumentCaptor.forClass(GoodInOrder.class);
        verify(goodInOrderRepository).save(goodCaptor.capture());
        GoodInOrder capturedGood = goodCaptor.getValue();
        assertThat(capturedGood.getGoodName()).isEqualTo(good.getGoodName());
        assertThat(capturedGood.getQuantity()).isEqualTo(good.getQuantity());
    }

}