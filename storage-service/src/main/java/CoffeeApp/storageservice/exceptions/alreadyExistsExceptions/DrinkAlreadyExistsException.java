package CoffeeApp.storageservice.exceptions.alreadyExistsExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DrinkAlreadyExistsException extends RuntimeException{

    public DrinkAlreadyExistsException(String message){
        super(message);
    }
}
