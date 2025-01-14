package CoffeeApp.sellingservice.services;

import CoffeeApp.sellingservice.models.GoodInOrder;
import CoffeeApp.sellingservice.repositories.GoodInOrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class GoodInOrderService {

    private final GoodInOrderRepository goodInOrderRepository;

    public List<GoodInOrder> findByOrderId(Integer id){
        return goodInOrderRepository.findByOrder_Id(id);
    }

    @Transactional
    public void addDrinkInOrder(GoodInOrder goodInOrder){
        goodInOrderRepository.save(goodInOrder);
    }
}
