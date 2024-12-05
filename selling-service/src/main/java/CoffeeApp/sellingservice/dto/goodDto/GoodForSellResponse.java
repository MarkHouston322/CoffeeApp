package CoffeeApp.sellingservice.dto.goodDto;

import CoffeeApp.sellingservice.dto.messages.GoodMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GoodForSellResponse {

    private List<GoodMessage> goods;
}
