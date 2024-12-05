package CoffeeApp.storageservice.exceptions.alreadyExistsExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IngredientAlreadyExistsException extends RuntimeException{

    public IngredientAlreadyExistsException(String message){
        super(message);
    }
}
