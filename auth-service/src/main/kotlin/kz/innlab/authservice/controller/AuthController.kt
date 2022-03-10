package kz.innlab.authservice.controller

import kz.innlab.authservice.model.payload.UserRequest
import kz.innlab.authservice.service.AuthService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@RestController
@RequestMapping("/auth")
class AuthController {

    @Autowired
    private lateinit var authService: AuthService

    @PostMapping("/sign-in")
    fun authenticateUserLogin(
        @Valid @RequestBody loginRequest: UserRequest,
        @RequestHeader headers: Map<String, String>
    ): ResponseEntity<*> {
        return ResponseEntity.ok(authService.authenticate(loginRequest))
    }
}
