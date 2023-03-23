package ua.com.foxminded.asharov.universityschedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    static final String ERROR = "error";

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "UniversityException occured")
    @ExceptionHandler(UniversityException.class)
    public ModelAndView handleUniversityException(Exception ex) {
        log.error("UniversityException");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ERROR);
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "NotFoundException occured")
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(Exception ex) {
        log.error("NotFoundException");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ERROR);
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ModelAndView exception(final Throwable throwable, final Model model) {
        log.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ERROR);
        modelAndView.setStatus(HttpStatus.UNAUTHORIZED);
        modelAndView.addObject("message", errorMessage);
        return modelAndView;
    }
   
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView exceptionF(final Throwable throwable, final Model model) {
        log.error("Exception during execution of SpringSecurity application", throwable);
        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ERROR);
        modelAndView.setStatus(HttpStatus.FORBIDDEN);
        modelAndView.addObject("message", errorMessage);
        return modelAndView;
    }
    
}
