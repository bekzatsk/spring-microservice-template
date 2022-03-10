package kz.innlab.userservice.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@ControllerAdvice
class ErrorHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    // TODO add MethodArgumentNotValidException handler
    // TODO remove such general handler
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun processValidationError(e: IllegalArgumentException?) {
        log.info("Returning HTTP 400 Bad Request", e)
    }
}
