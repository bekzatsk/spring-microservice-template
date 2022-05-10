package kz.innlab.authservice.controller

import kz.innlab.authservice.model.Role
import kz.innlab.authservice.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import kotlin.collections.ArrayList

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */

@RestController
@RequestMapping("/roles")
class RoleController {

    @Autowired
    private lateinit var roleService: RoleService

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    fun getList(): ArrayList<Role> {
        return roleService.getList()
    }

//    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
//    @PostMapping("/create")
//    fun createUser(@Valid @RequestBody user: NewUserRequest): UUID? {
//        return userService.create(user)
//    }
}
