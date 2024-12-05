package CoffeeApp.financialservice.in.Coffee.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LastSessionIsOpenedException extends RuntimeException {

    public LastSessionIsOpenedException(String message){
        super(message);
    }
}
