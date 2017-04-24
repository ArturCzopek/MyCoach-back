package pl.arturczopek.mycoach.utils.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @ExceptionHandler(Throwable.class)
    @ResponseBody public ResponseEntity<ErrorMessage>  handleOtherExceptions(HttpServletRequest req, Exception ex) {
        return getErrorMessage(req, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class })
    @ResponseBody public ResponseEntity<ErrorMessage>  notFoundEntityHandler(HttpServletRequest req, Exception ex) {
        return getErrorMessage(req, ex, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidAttributeValueException.class)
    @ResponseBody public ResponseEntity<ErrorMessage> invalidAttributeValueExceptionHandler(HttpServletRequest req, Exception ex) {
        return getErrorMessage(req, ex, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({DuplicateKeyException.class, IOException.class, DataIntegrityViolationException.class})
    @ResponseBody public ResponseEntity<ErrorMessage> duplicateKeyExceptionHandler(HttpServletRequest req, Exception ex) {
        return getErrorMessage(req, ex, HttpStatus.NOT_ACCEPTABLE);
    }

    private ResponseEntity<ErrorMessage> getErrorMessage(HttpServletRequest req, Exception ex, HttpStatus httpStatus) {
        log.error(req.getMethod() + " " + req.getRequestURL().toString(), ex);
        return ResponseEntity.status(httpStatus).body(new ErrorMessage(ex.getMessage(), req.getRequestURL().toString(), ex.toString()));
    }
}
