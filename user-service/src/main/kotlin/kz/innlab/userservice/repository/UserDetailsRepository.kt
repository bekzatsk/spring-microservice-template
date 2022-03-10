package kz.innlab.userservice.repository

import kz.innlab.userservice.model.UserDetails
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface UserDetailsRepository: JpaRepository<UserDetails, UUID> {

    fun findByUserId(userId: UUID): Optional<UserDetails>

}
