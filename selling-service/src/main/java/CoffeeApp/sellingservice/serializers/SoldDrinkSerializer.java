package CoffeeApp.sellingservice.serializers;

import CoffeeApp.sellingservice.dto.messages.SoldDrinkMessage;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;

public class SoldDrinkSerializer implements Serializer<SoldDrinkMessage> {
    @Override
    public byte[] serialize(String s, SoldDrinkMessage soldDrinkMessage) {
        return convertSoldDrinkToJsonBytes(soldDrinkMessage);
    }

    private byte[] convertSoldDrinkToJsonBytes(SoldDrinkMessage soldDrinkMessage){
        Gson gson = new Gson();
        String json = gson.toJson(soldDrinkMessage);
        return json.getBytes();
    }
}
