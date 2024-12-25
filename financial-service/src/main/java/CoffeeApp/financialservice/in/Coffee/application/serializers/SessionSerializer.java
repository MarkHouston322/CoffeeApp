package CoffeeApp.financialservice.in.Coffee.application.serializers;

import CoffeeApp.financialservice.in.Coffee.application.dto.messages.ProceedSessionMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


public class SessionSerializer implements Serializer<ProceedSessionMessage> {

    private final Gson gson;

    public SessionSerializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public byte[] serialize(String s, ProceedSessionMessage proceedSessionMessage) {
        return gson.toJson(proceedSessionMessage).getBytes(StandardCharsets.UTF_8);
    }

}
