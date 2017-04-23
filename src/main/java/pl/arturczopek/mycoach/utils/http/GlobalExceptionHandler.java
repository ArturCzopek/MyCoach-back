package pl.arturczopek.mycoach.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.management.InvalidAttributeValueException;
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
        return getErrorMessage(req, ex, "Not found page");
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Not found object")
    @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class })
    @ResponseBody ErrorMessage notFoundEntityHandler(HttpServletRequest req, Exception ex) {
        return getErrorMessage(req, ex, "Not found object");
    }

    @ResponseStatus(value= HttpStatus.NOT_ACCEPTABLE, reason = "Operation failed. Probably object already exists")
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody public ErrorMessage dataIntegrityViolationHandler(HttpServletRequest req, Exception ex) {
        return getErrorMessage(req, ex, "Operation failed. Probably object already exists");
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Operation failed. Probably file is too big")
    @ExceptionHandler(IOException.class)
    @ResponseBody public ErrorMessage ioExceptionHandler(HttpServletRequest req, Exception ex) {
        return getErrorMessage(req, ex, "Operation failed. Probably file is too big");
    }

    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "Operation failed. Probably params are wrong")
    @ExceptionHandler(InvalidAttributeValueException.class)
    @ResponseBody public ErrorMessage invalidAttributeValueExceptionHandler(HttpServletRequest req, Exception ex) {
        return getErrorMessage(req, ex, "Operation failed. Probably params are wrong");
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "Operation failed. Probably params are duplicated")
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody public ErrorMessage duplicateKeyExceptionHandler(HttpServletRequest req, Exception ex) {
        return getErrorMessage(req, ex, "Operation failed. Probably params are duplicated");
    }

    private ErrorMessage getErrorMessage(HttpServletRequest req, Exception ex, String message) {
        log.error(req.getMethod() + " " + req.getRequestURL().toString(), ex);
        return new ErrorMessage(message, req.getRequestURL().toString(), ex.toString());
    }
}
