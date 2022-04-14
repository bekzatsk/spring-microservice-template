package kz.innlab.userservice.service

import kz.innlab.userservice.client.AuthServiceClient
import kz.innlab.userservice.model.Account
import kz.innlab.userservice.model.payload.UserRequest
import kz.innlab.userservice.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*


/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@Service
class AccountServiceImpl: AccountService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var authClient: AuthServiceClient

    @Autowired
    lateinit var repository: AccountRepository

    override fun findByName(name: String): Optional<Account> {
        return repository.findByFirstName(name)
    }

    override fun create(user: UserRequest): Optional<Account> {
        val existing = repository.findByFirstName(user.username!!)
        if (existing.isPresent) {
            log.warn("new account has been created: " + user.username)
        }

        var userId: UUID? = null
        try {
            userId = authClient.createUser(user)
        } catch (e: Exception) {
            log.error("500 Error ${e.message}", e)
            throw Exception("eeeee")
        }

        val userDetails = Account()
        userDetails.userId = userId
        userDetails.firstName = user.firstName
        userDetails.lastName = user.lastName
        userDetails.middleName = user.middleName
        userDetails.avatar = user.avatar
        repository.save(userDetails)

        log.info("new account has been created: " + user.username)
        return Optional.of(userDetails)
    }

    override fun saveChanges(name: String, update: Account) {
        TODO("Not yet implemented")
//        val userDetails = repository.findByUserId(userId)
//        Assert.notNull(userDetails, "can't find account with name ${update.contactDetails}")
//
//        userDetails.get().contactDetails = update.contactDetails
//
//        log.debug("account {} changes has been saved", userDetails.get().contactDetails)
//        repository.save(userDetails.get())
    }
}
