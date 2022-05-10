package kz.innlab.userservice.client

import kz.innlab.userservice.dto.Status
import kz.innlab.userservice.dto.UserDTO
import kz.innlab.userservice.model.payload.UserRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@FeignClient(name = "auth-service")
interface AuthServiceClient {
    @GetMapping("/uaa/users/user/{id}")
    fun getUserById(@PathVariable id: UUID): Optional<UserDTO>

    @GetMapping("/uaa/users/user-name/{username}")
    fun getUserByUserName(@PathVariable username: String): Optional<UserDTO>

    @PostMapping("/uaa/users/create")
    fun createUser(user: UserRequest): UUID?

    @PutMapping("/uaa/users/update")
    fun updateUser(user: UserRequest): UUID?

    @PostMapping("/uaa/users/list")
    fun getUserListByIds(ids: List<UUID>): ArrayList<UserDTO>

    @PostMapping("/uaa/users/list/school/{schoolId}")
    fun getUserListBySchoolId(@PathVariable schoolId: UUID): List<UserDTO>

    @DeleteMapping("/uaa/users/remove/{id}")
    fun removeToTrashUser(@PathVariable id: UUID): Status
}
