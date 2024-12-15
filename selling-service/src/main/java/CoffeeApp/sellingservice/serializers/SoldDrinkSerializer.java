package CoffeeApp.sellingservice.serializers;

import CoffeeApp.sellingservice.dto.messages.SoldDrinkMessage;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class SoldDrinkSerializer implements Serializer<SoldDrinkMessage> {

    private final Gson gson;

    @Override
    public byte[] serialize(String s, SoldDrinkMessage soldDrinkMessage) {
        return convertSoldDrinkToJsonBytes(soldDrinkMessage);
    }

    private byte[] convertSoldDrinkToJsonBytes(SoldDrinkMessage soldDrinkMessage){
        String json = gson.toJson(soldDrinkMessage);
        return json.getBytes();
    }
}
