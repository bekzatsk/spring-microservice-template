package kz.innlab.authservice.exceptionHandler

import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 05.04.2022
 */
@RequiredArgsConstructor
class ErrorResponse {
    var status: HttpStatus? = null
    var errorMsg: String? = null
    var developerMsg: String? = null
}
