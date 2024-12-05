package CoffeeApp.employeesservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BonusAlreadyExistsException extends RuntimeException{

    public BonusAlreadyExistsException(String message){
        super(message);
    }
}
