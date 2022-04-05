package kz.innlab.userservice.service

import kz.innlab.userservice.client.AuthServiceClient
import kz.innlab.userservice.model.Account
import kz.innlab.userservice.model.User
import kz.innlab.userservice.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.Assert
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
        return repository.findByName(name)
    }

    override fun create(user: User): Optional<Account> {
        val existing = repository.findByName(user.username!!)
        if (existing.isPresent) {
            log.warn("new account has been created: " + user.username)
        }

        val userId = authClient.createUser(user)

        val userDetails = Account()
        userDetails.userId = userId
        userDetails.name = user.username
        userDetails.contactDetails = ""
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
