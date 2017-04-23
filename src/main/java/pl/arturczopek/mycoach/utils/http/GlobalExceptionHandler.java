package pl.arturczopek.mycoach.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author Artur Czopek
 * @Date 19-10-2016
 */

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Not found page")
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ErrorMessage handleOtherExceptions(HttpServletRequest req, Exception ex) {
        String message = "Not found page";
        log.error(req.getMethod() + " " + req.getRequestURL().toString(), ex);
        return new ErrorMessage(message, req.getRequestURL().toString(), ex.toString());
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Not found object")
    @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class })
    @ResponseBody ErrorMessage notFoundEntityHandler(HttpServletRequest req, Exception ex) {
        String message = "Not found object";
        log.error(req.getMethod() + " " + req.getRequestURL().toString(), ex);
        return new ErrorMessage(message, req.getRequestURL().toString(), ex.toString());
    }

    @ResponseStatus(value= HttpStatus.CONFLICT, reason = "Operation failed. Probably object already exists")
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody public ErrorMessage dataIntegrityViolationHandler(HttpServletRequest req, Exception ex) {
        String message = "Operation failed. Probably object already exists";
        log.error(req.getMethod() + " " + req.getRequestURL().toString(), ex);
        return new ErrorMessage(message, req.getRequestURL().toString(), ex.toString());
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Operation failed. Probably ile is too big")
    @ExceptionHandler(IOException.class)
    @ResponseBody public ErrorMessage ioExceptionHandler(HttpServletRequest req, Exception ex) {
        String message = "Operation failed. Probably ile is too big";
        log.error(req.getMethod() + " " + req.getRequestURL().toString(), ex);
        return new ErrorMessage(message, req.getRequestURL().toString(), ex.toString());
    }
}
