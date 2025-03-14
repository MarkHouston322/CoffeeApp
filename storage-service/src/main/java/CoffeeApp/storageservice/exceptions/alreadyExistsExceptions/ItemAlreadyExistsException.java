package CoffeeApp.storageservice.exceptions.alreadyExistsExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ItemAlreadyExistsException extends RuntimeException{

    public ItemAlreadyExistsException(String message){
        super(message);
    }
}
