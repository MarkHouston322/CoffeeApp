package CoffeeApp.financialservice.in.Coffee.application.serializers;

import CoffeeApp.financialservice.in.Coffee.application.dto.messages.ProceedSessionMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.messages.ProceedTransactionMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


public class TransactionSerializer implements Serializer<ProceedTransactionMessage> {

    private final Gson gson;

    public TransactionSerializer() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public byte[] serialize(String s, ProceedTransactionMessage proceedTransactionMessage) {
        return gson.toJson(proceedTransactionMessage).getBytes(StandardCharsets.UTF_8);
    }

}
