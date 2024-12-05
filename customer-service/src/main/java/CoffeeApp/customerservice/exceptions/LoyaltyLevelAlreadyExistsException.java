package CoffeeApp.customerservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoyaltyLevelAlreadyExistsException extends RuntimeException{

    public LoyaltyLevelAlreadyExistsException(String message){
        super(message);
    }
}
