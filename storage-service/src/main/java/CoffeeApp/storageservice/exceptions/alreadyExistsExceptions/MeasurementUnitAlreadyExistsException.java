package CoffeeApp.storageservice.exceptions.alreadyExistsExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MeasurementUnitAlreadyExistsException extends RuntimeException{

    public MeasurementUnitAlreadyExistsException(String message){
        super(message);
    }
}
