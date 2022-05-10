package kz.innlab.authservice.service

import kz.innlab.authservice.dto.Status
import kz.innlab.authservice.model.User
import kz.innlab.authservice.model.UsersRoles
import kz.innlab.authservice.model.payload.NewUserRequest
import kz.innlab.authservice.repository.RoleRepository
import kz.innlab.authservice.repository.UserRepository
import kz.innlab.authservice.repository.UsersRolesRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@Service
class UserServiceImpl: UserService {

    private val log = LoggerFactory.getLogger(javaClass)

    private val encoder = BCryptPasswordEncoder()

    @Autowired
    lateinit var repository: UserRepository

    @Autowired
    lateinit var usersRoles: UsersRolesRepository

    @Autowired
    lateinit var roleRepository: RoleRepository

    override fun create(user: NewUserRequest): UUID? {
        if (user.username.isNotBlank()) {
            val existing = repository.findByUsernameIgnoreCase(user.username)
            existing.ifPresent { it -> throw IllegalArgumentException("user already exists: " + it.username) }

            val newUser = User()
            newUser.username = user.username
            newUser.email = user.email
            newUser.password = encoder.encode(user.password)
            newUser.enabled = user.enabled
            newUser.emailVerified = true
            newUser.schoolId = user.schoolId

            repository.save(newUser)
            if (newUser.id != null) {
                if (user.roles.isNotEmpty()) {
                    for (role in user.roles) {
                        val roleCandidate = roleRepository.findByNameIgnoreCaseAndDeletedAtIsNull(role.uppercase())
                        if (roleCandidate.isPresent) {
                            val newRole = UsersRoles()
                            newRole.userId = newUser.id
                            newRole.roleId = roleCandidate.get().id
                            usersRoles.save(newRole)
                        }
                    }
                }
                log.info("new user has been created: {}", user.username)
                return newUser.id!!
            }
        }
        return null
    }

    override fun saveChanges(user: NewUserRequest): UUID? {
        if (user.id != null) {
            val userCandidate = repository.findById(user.id!!)
            if (userCandidate.isPresent) {
                userCandidate.get().username = user.username
                userCandidate.get().email = user.email
                //TODO User schoolId
                if (user.password.isNotBlank()) {
                    userCandidate.get().password = encoder.encode(user.password.trim())
                }
                repository.save(userCandidate.get())

                if (user.roles.isNotEmpty()) {
                    usersRoles.deleteAllByUserId(userCandidate.get().id!!)

                    for (role in user.roles) {
                        val roleCandidate = roleRepository.findByNameIgnoreCaseAndDeletedAtIsNull(role.uppercase())
                        if (roleCandidate.isPresent) {
                            val newRole = UsersRoles()
                            newRole.userId = userCandidate.get().id
                            newRole.roleId = roleCandidate.get().id
                            usersRoles.save(newRole)
                        }
                    }
                }
                return userCandidate.get().id
            } else {
                return null
            }
        }
        return null
    }

    override fun getUserById(id: UUID): Optional<User> {
        return repository.findById(id)
    }

    override fun getUserByUserName(username: String): Optional<User> {
        return repository.findByUsernameIgnoreCase(username)
    }

    override fun getUserListByIds(ids: List<UUID>): ArrayList<User> {
        return repository.findAllByIdInAndDeletedAtIsNull(ids)
    }

    override fun getUserListBySchoolId(schoolId: UUID): List<User> {
        return repository.findAllBySchoolIdAndDeletedAtIsNull(schoolId)
    }

    override fun moveToTrash(id: UUID): Status {
        val status = Status()
        val user = repository.findById(id)
        if (user.isPresent) {
            user.get().deletedAt = Timestamp(System.currentTimeMillis())
            repository.save(user.get())

            status.status = 1
        }
        return status
    }

    override fun delete(id: UUID): Status {
        val status = Status()

        return status
    }

    override fun getUserListBySchool(schoolId: UUID): List<User> {
        return repository.findAllBySchoolId(schoolId)
    }

}
