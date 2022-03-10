package kz.innlab.userservice.service

import io.jsonwebtoken.lang.Assert
import kz.innlab.userservice.client.AuthServiceClient
import kz.innlab.userservice.model.User
import kz.innlab.userservice.model.UserDetails
import kz.innlab.userservice.repository.UserDetailsRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
class UserServiceImpl: UserService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var authClient: AuthServiceClient

    @Autowired
    lateinit var repository: UserDetailsRepository

    override fun findByUserId(userId: UUID): Optional<UserDetails> {
        return repository.findByUserId(userId)
    }

    override fun create(user: User): Optional<UserDetails> {
        val existing = repository.findByUserId(user.id!!)
        Assert.isNull(existing, "account already exists: " + user.username)

        authClient.createUser(user)

        val userDetails = UserDetails()
        userDetails.contactDetails = ""
        repository.save(userDetails)

        log.info("new account has been created: " + user.username)
        return Optional.of(userDetails)
    }

    override fun saveChanges(userId: UUID, update: UserDetails) {
        TODO("Not yet implemented")
        val userDetails = repository.findByUserId(userId)
        Assert.notNull(userDetails, "can't find account with name ${update.contactDetails}")

        userDetails.get().contactDetails = update.contactDetails

        log.debug("account {} changes has been saved", userDetails.get().contactDetails)
        repository.save(userDetails.get())
    }
}
