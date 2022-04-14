package kz.innlab.userservice.client

import kz.innlab.userservice.model.payload.UserRequest
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@FeignClient(name = "auth-service")
open interface AuthServiceClient {
    @PostMapping("/uaa/users/create")
    fun createUser(user: UserRequest): UUID?
}
