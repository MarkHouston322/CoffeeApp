package CoffeeApp.sellingservice.serializers;

import CoffeeApp.sellingservice.dto.messages.ProceedOrderMessage;
import com.google.gson.*;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;


@AllArgsConstructor
public class OrderSerializer implements Serializer<ProceedOrderMessage> {


    @Override
    public byte[] serialize(String s, ProceedOrderMessage proceedOrderMessage) {
        return convertOrderToJsonBytes(proceedOrderMessage);
    }

    private byte[] convertOrderToJsonBytes(ProceedOrderMessage proceedOrderMessage){
       Gson gson = new Gson();
       String json = gson.toJson(proceedOrderMessage);
       return json.getBytes();
    }
}
