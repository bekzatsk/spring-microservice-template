package kz.innlab.authservice.service

import kz.innlab.authservice.model.User
import kz.innlab.authservice.model.payload.NewUserRequest
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface UserService {

    fun create(user: NewUserRequest): UUID?

}
