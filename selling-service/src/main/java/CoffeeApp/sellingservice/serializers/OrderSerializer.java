package CoffeeApp.sellingservice.serializers;

import CoffeeApp.sellingservice.dto.messages.ProceedOrderMessage;
import com.google.gson.*;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


public class OrderSerializer implements Serializer<ProceedOrderMessage> {

    private final Gson gson;

    public OrderSerializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public byte[] serialize(String s, ProceedOrderMessage proceedOrderMessage) {
        return gson.toJson(proceedOrderMessage).getBytes(StandardCharsets.UTF_8);
    }
}
