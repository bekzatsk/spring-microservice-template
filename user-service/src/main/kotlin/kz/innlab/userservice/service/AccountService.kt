package kz.innlab.userservice.service

import kz.innlab.userservice.model.Account
import kz.innlab.userservice.model.User
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface AccountService {

    fun findByName(name: String): Optional<Account>
    fun create(user: User): Optional<Account>
    fun saveChanges(name: String, userDetails: Account)

}
