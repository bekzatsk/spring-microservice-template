package kz.innlab.userservice.service

import kz.innlab.userservice.model.User
import kz.innlab.userservice.model.UserDetails
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface UserService {

    fun findByUserId(userId: UUID): Optional<UserDetails>
    fun create(user: User): Optional<UserDetails>
    fun saveChanges(userId: UUID, userDetails: UserDetails)

}
