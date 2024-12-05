package CoffeeApp.sellingservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class PaymentMethodAlreadyExistsException extends RuntimeException{

    public PaymentMethodAlreadyExistsException(String message){
        super(message);
    }
}
