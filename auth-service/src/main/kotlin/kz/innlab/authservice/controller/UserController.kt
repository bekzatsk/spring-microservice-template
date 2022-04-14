package kz.innlab.authservice.controller

import kz.innlab.authservice.model.User
import kz.innlab.authservice.model.payload.NewUserRequest
import kz.innlab.authservice.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import java.util.*
import javax.validation.Valid

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/current")
    fun getUser(principal: Principal): Principal {
        return principal
    }

    @PreAuthorize("#oauth2.hasScope('server') or hasAnyRole('ADMIN')")
    @PostMapping("/create")
    fun createUser(@Valid @RequestBody user: NewUserRequest): UUID? {
        return userService.create(user)
    }
}
