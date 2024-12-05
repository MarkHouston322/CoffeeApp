package CoffeeApp.storageservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class NullOrEmptyParamException extends RuntimeException{

    public NullOrEmptyParamException(String message){
        super(message);
    }
}
