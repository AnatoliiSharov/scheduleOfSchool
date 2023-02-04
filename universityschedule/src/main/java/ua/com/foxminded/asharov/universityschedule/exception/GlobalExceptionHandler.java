package ua.com.foxminded.asharov.universityschedule.exception;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

import org.apache.coyote.Response;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "UniversityException occured")
    @ExceptionHandler(UniversityException.class)
    public ModelAndView handleUniversityException(Exception ex) {
        log.error("UniversityException");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "NotFoundException occured")
    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(Exception ex) {
        log.error("NotFoundException");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("error");
        modelAndView.setStatus(HttpStatus.BAD_REQUEST);
        modelAndView.addObject("message", ex.getMessage());
        return modelAndView;
    }

}
