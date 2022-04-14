package kz.innlab.authservice.service

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
        if (user.username != null) {
            val existing = repository.findByUsernameIgnoreCase(user.username!!)
            existing.ifPresent { it -> throw IllegalArgumentException("user already exists: " + it.username) }

            val newUser = User()
            newUser.username = user.username
            newUser.email = user.email
            newUser.password = encoder.encode(user.password)
            newUser.enabled = user.enabled
            newUser.emailVerified = true

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

}
