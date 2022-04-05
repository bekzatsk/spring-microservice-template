package kz.innlab.userservice.repository

import kz.innlab.userservice.model.Account
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface AccountRepository: JpaRepository<Account, UUID> {

    fun findByName(name: String): Optional<Account>
    fun findByUserId(userId: UUID): Optional<Account>

}
