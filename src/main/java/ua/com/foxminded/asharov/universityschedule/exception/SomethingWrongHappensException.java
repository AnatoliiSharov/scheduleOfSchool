package ua.com.foxminded.asharov.universityschedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "wrong ownername or owner")
@SuppressWarnings("serial")
public class SomethingWrongHappensException extends RuntimeException{
    
    
    public SomethingWrongHappensException(String errorMessage) {
        super(errorMessage);
    }
    

}
