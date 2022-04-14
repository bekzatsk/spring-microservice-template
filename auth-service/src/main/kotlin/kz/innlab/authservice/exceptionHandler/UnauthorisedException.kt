package kz.innlab.authservice.exceptionHandler

import org.springframework.http.HttpStatus

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 05.04.2022
 */
class UnauthorisedException(message: String?, developerMessage: String?) :
    RuntimeException(message) {
    private val errorResponse: ErrorResponse = ErrorResponse()

    init {
        errorResponse.developerMsg = developerMessage
        errorResponse.errorMsg = message
        errorResponse.status = HttpStatus.UNAUTHORIZED
    }
}
