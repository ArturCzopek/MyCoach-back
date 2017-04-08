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

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Nie znaleziono strony")
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ErrorMessage handleOtherExceptions(HttpServletRequest req, Exception ex) {
        String message = "Nie znaleziono strony";
        log.error(req.getMethod() + " " + req.getRequestURL().toString(), ex);
        return new ErrorMessage(message, req.getRequestURL().toString(), ex.toString());
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Nie znaleziono obiektu")
    @ExceptionHandler({EntityNotFoundException.class, EmptyResultDataAccessException.class })
    @ResponseBody ErrorMessage notFoundEntityHandler(HttpServletRequest req, Exception ex) {
        String message = "Nie znaleziono obiektu";
        log.error(req.getMethod() + " " + req.getRequestURL().toString(), ex);
        return new ErrorMessage(message, req.getRequestURL().toString(), ex.toString());
    }

    @ResponseStatus(value= HttpStatus.CONFLICT)
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody public ErrorMessage dataIntegrityViolationHandler(HttpServletRequest req, Exception ex) {
        String message = "Operacja nieudana, prawdopodobnie obiekt juz istnieje";
        log.error(req.getMethod() + " " + req.getRequestURL().toString(), ex);
        return new ErrorMessage(message, req.getRequestURL().toString(), ex.toString());
    }

    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(IOException.class)
    @ResponseBody public ErrorMessage IOExceptionHandler(HttpServletRequest req, Exception ex) {
        String message = "Operacja nieudana, prawdopodobnie plik za duży";
        log.error(req.getMethod() + " " + req.getRequestURL().toString(), ex);
        return new ErrorMessage(message, req.getRequestURL().toString(), ex.toString());
    }
}
