package ua.com.foxminded.asharov.universityschedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The resource you're looking for was not found, and that's weird")
public class ResourceNotFoundException  extends RuntimeException {

    public ResourceNotFoundException(String whatHappend) {
        super("during run  have not found resorce, try again");
    }
    
    
}
