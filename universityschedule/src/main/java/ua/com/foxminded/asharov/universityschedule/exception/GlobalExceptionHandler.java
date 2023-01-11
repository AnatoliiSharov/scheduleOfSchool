package ua.com.foxminded.asharov.universityschedule.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "SomethingWrongHappensException occured")
    @ExceptionHandler(SomethingWrongHappensException.class)
    public ModelAndView handleSomethingWrongHappensException(Exception ex) {
        logger.error("SomethingWrongHappensException");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.addObject("massage", ex.getMessage());
        return modelAndView;
    }
    
}
