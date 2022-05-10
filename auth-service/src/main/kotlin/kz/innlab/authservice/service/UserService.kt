package kz.innlab.authservice.service

import kz.innlab.authservice.dto.Status
import kz.innlab.authservice.model.User
import kz.innlab.authservice.model.payload.NewUserRequest
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface UserService {

    fun create(user: NewUserRequest): UUID?
    fun saveChanges(user: NewUserRequest): UUID?
    fun getUserById(id: UUID): Optional<User>
    fun getUserByUserName(username: String): Optional<User>
    fun getUserListByIds(ids:  List<UUID>): ArrayList<User>
    fun getUserListBySchoolId(schoolId: UUID): List<User>
    fun moveToTrash(id: UUID): Status
    fun delete(id: UUID): Status
    fun getUserListBySchool(schoolId: UUID): List<User>

}
