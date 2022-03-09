package kz.innlab.authservice.service

import kz.innlab.authservice.model.User
import kz.innlab.authservice.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

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

    override fun create(user: User) {
        val existing = repository.findByUsernameIgnoreCase(user.username)
        existing.ifPresent { it -> throw IllegalArgumentException("user already exists: " + it.username) }

        val hash: String = encoder.encode(user.password)
        user.password = hash

        repository.save(user)

        log.info("new user has been created: {}", user.username)
    }

}
