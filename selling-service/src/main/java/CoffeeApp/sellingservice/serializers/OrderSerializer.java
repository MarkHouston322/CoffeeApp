package CoffeeApp.sellingservice.serializers;

import CoffeeApp.sellingservice.dto.messages.ProceedOrderMessage;
import com.google.gson.*;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class OrderSerializer implements Serializer<ProceedOrderMessage> {

    private final Gson gson;


    @Override
    public byte[] serialize(String s, ProceedOrderMessage proceedOrderMessage) {
        return convertOrderToJsonBytes(proceedOrderMessage);
    }

    private byte[] convertOrderToJsonBytes(ProceedOrderMessage proceedOrderMessage){
       String json = gson.toJson(proceedOrderMessage);
       return json.getBytes();
    }
}
