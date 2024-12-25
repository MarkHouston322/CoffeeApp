package CoffeeApp.employeesservice.serializers;

import CoffeeApp.employeesservice.dto.messages.ProceedSalaryMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class SalarySerializer implements Serializer<ProceedSalaryMessage> {

    private final Gson gson;

    public SalarySerializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public byte[] serialize(String s, ProceedSalaryMessage proceedSalaryMessage) {
        return gson.toJson(proceedSalaryMessage).getBytes(StandardCharsets.UTF_8);
    }
}
