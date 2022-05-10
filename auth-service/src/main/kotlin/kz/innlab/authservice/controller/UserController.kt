package kz.innlab.authservice.controller

import kz.innlab.authservice.dto.Status
import kz.innlab.authservice.model.User
import kz.innlab.authservice.model.payload.NewUserRequest
import kz.innlab.authservice.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/user/{id}")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun getUserById(@PathVariable(value = "id") id: UUID): Optional<User> {
        return userService.getUserById(id)
    }

    @GetMapping("/user-name/{username}")
    @PreAuthorize("#oauth2.hasScope('server')")
    fun getUserByUserName(@PathVariable(value = "username") username: String): Optional<User> {
        return userService.getUserByUserName(username)
    }

    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    @PostMapping("/list")
    fun getUserListByIds(@Valid @RequestBody ids: List<UUID>): ArrayList<User> {
        return userService.getUserListByIds(ids)
    }

    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    @PostMapping("/list/school/{schoolId}")
    fun getUserListBySchoolId(@PathVariable schoolId: UUID): List<User> {
        return userService.getUserListBySchoolId(schoolId)
    }

    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    @PostMapping("/create")
    fun createUser(@Valid @RequestBody user: NewUserRequest): UUID? {
        return userService.create(user)
    }

    @PutMapping("/update")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun updateUser(@Valid @RequestBody user: NewUserRequest): UUID? {
        return userService.saveChanges(user)
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun removeToTrashUser(@PathVariable id: UUID): Status {
        return userService.moveToTrash(id)
    }

    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    @GetMapping("/list/{schoolId}")
    fun getUserListBySchool(@PathVariable schoolId: UUID): List<User> {
        return userService.getUserListBySchool(schoolId)
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/has-role")
    fun createUser(@Valid @RequestBody roles: List<String>, authentication: Authentication): Status {
        val status = Status()
        val user = userService.getUserByUserName(authentication.name)
        if (user.isPresent) {
            if (roles.isEmpty()) {
                status.status = 1
                return status
            }
            for (role in roles) {
                if (user.get().rolesCollection.stream()
                    .anyMatch { it.name.uppercase().trim() == role.uppercase().trim() }) {
                    status.status = 1
                    return status
                }
            }
        }
        return status
    }
}
