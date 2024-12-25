package CoffeeApp.sellingservice.serializers;

import CoffeeApp.sellingservice.dto.messages.SoldDrinkMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class SoldDrinkSerializer implements Serializer<SoldDrinkMessage> {

    private final Gson gson;

    public SoldDrinkSerializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public byte[] serialize(String s, SoldDrinkMessage soldDrinkMessage) {
        return gson.toJson(soldDrinkMessage).getBytes(StandardCharsets.UTF_8);
    }

}
