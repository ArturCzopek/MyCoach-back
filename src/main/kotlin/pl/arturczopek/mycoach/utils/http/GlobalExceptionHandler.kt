package pl.arturczopek.mycoach.utils.http

import mu.KLogging
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import pl.arturczopek.mycoach.exception.InactiveUserException
import pl.arturczopek.mycoach.exception.InvalidPropsException
import pl.arturczopek.mycoach.exception.WrongPermissionException
import java.io.IOException
import javax.persistence.EntityNotFoundException
import javax.servlet.http.HttpServletRequest

/**
 * @Author Artur Czopek
 * @Date 07-05-2017
 */
@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(Throwable::class)
    @ResponseBody
    fun handleOtherExceptions(req: HttpServletRequest, ex: Exception) = getErrorMessage(req, ex, HttpStatus.NOT_FOUND)

    @ExceptionHandler(EntityNotFoundException::class, EmptyResultDataAccessException::class)
    @ResponseBody
    fun notFoundEntityHandler(req: HttpServletRequest, ex: Exception) = getErrorMessage(req, ex, HttpStatus.NOT_FOUND)

    @ExceptionHandler(IOException::class, InvalidPropsException::class)
    @ResponseBody
    fun duplicateKeyHandler(req: HttpServletRequest, ex: Exception) = getErrorMessage(req, ex, HttpStatus.NOT_ACCEPTABLE)

    @ExceptionHandler(WrongPermissionException::class, InactiveUserException::class)
    @ResponseBody
    fun wrongPermissionHandler(req: HttpServletRequest, ex: Exception) = getErrorMessage(req, ex, HttpStatus.FORBIDDEN)

    private fun getErrorMessage(req: HttpServletRequest, ex: Exception, status: HttpStatus): ResponseEntity<ErrorMessage> {
        logger.error(req.method, req.requestURL.toString(), ex)
        return ResponseEntity
                .status(status)
                .body(ErrorMessage(
                        ex.message!!,
                        req.requestURL.toString(),
                        ex.toString()
                ))
    }

    companion object : KLogging()
}

