package kz.innlab.userservice.service

import kz.innlab.userservice.dto.Status
import kz.innlab.userservice.model.Account
import kz.innlab.userservice.model.payload.UserRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface AccountService {

    fun findByName(name: String): Optional<UserRequest>
    fun getAccountById(id: UUID): Optional<UserRequest>
    fun getList(page: PageRequest, params: MutableMap<String, String>): Page<Account>
    fun create(user: UserRequest): Status
    fun saveChanges(user: UserRequest): Status
    fun moveToTrash(id: UUID): Status
    fun delete(id: UUID): Status
    fun getUsersBySchool(schoolId: UUID): List<Account>
}
