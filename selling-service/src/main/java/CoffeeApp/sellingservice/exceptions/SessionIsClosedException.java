package CoffeeApp.sellingservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SessionIsClosedException extends RuntimeException{

    public SessionIsClosedException(String message){
        super(message);
    }
}
