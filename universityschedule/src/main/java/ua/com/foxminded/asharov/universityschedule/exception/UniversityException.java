package ua.com.foxminded.asharov.universityschedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "wrong ownername or owner occur during run")
@SuppressWarnings("serial")
public class UniversityException extends RuntimeException {

    public UniversityException(String reason) {
        super("something happend with treatment " + reason + ", try again please");
    }

}
