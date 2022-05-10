package kz.innlab.userservice.service

import kz.innlab.userservice.client.AuthServiceClient
import kz.innlab.userservice.dto.Status
import kz.innlab.userservice.dto.UserDTO
import kz.innlab.userservice.model.Account
import kz.innlab.userservice.model.payload.UserRequest
import kz.innlab.userservice.repository.AccountRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.util.Assert
import java.sql.Timestamp
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

    override fun findByName(name: String): Optional<UserRequest> {
        val user = authClient.getUserByUserName(name)
        if (user.isPresent) {
            val account = repository.findByUserId(user.get().id!!)
            if (account.isPresent) {
                return Optional.of(getUserRequestFromAccountAndUser(account.get(), user.get()))
            }
        }
        return Optional.empty()
    }

    override fun getAccountById(id: UUID): Optional<UserRequest> {
        val account = repository.findById(id)
        if (account.isPresent) {
            val user = authClient.getUserById(account.get().userId!!)
            if (user.isPresent) {
                return Optional.of(getUserRequestFromAccountAndUser(account.get(), user.get()))
            }
        }
        return Optional.empty()
    }

    private fun getUserRequestFromAccountAndUser(account: Account, user: UserDTO): UserRequest {
        val result = UserRequest()
        result.id = account.id
        result.schoolId = user.schoolId
        result.firstName = account.firstName
        result.lastName = account.lastName
        result.middleName = account.middleName
        result.fio = account.fio
        result.username = user.username
        result.email = user.email
        result.avatar = account.avatar
        result.enabled = user.enabled
        result.roles = user.roles
        return result
    }

    override fun getList(page: PageRequest, params: MutableMap<String, String>): Page<Account> {
        // TODO filterdi duristap jazu kerek
        val result = if (
            params.containsKey("search")
            && params.containsKey("role")
            && !params["search"].isNullOrBlank()
            && !params["role"].isNullOrBlank()
        ) {
            repository.getAccountListByRoleAndSearch(params["r"]!!.trim().lowercase(), "%${params["search"]!!.trim().lowercase()}%", page)
        } else if (params.containsKey("search") && !params["search"].isNullOrBlank()) {
            repository.getAccountListBySearch("%${params["search"]!!.trim().lowercase()}%", page)
        } else if (params.containsKey("role") && !params["role"].isNullOrBlank()) {
            repository.getAccountListByRole(params["role"]!!.trim().lowercase(), page)
        } else {
            repository.getAccountList(page)
        }
        val users = authClient.getUserListByIds(result.content.map { it.userId }.toList() as List<UUID>)

        for (item in result.content) {
            val userFilter = users.filter { it.id == item.userId }
            if (userFilter.isNotEmpty()) {
                item.roles = userFilter.first().roles
                item.email = userFilter.first().email
            }
        }

        return result
    }

    override fun create(user: UserRequest): Status {
        val status = Status()
        val existing = repository.findByFirstName(user.username)
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
        status.status = 1
        status.value = Optional.of(userDetails)
        return status
    }

    override fun saveChanges(user: UserRequest): Status {
        val status = Status()
        Assert.notNull(user.id, "ID can't null")

        if (user.id != null) {
            val userDetails = repository.findById(user.id!!)
            if (userDetails.isPresent) {
                try {
                    user.id = userDetails.get().userId
                    val userId = authClient.updateUser(user)
                    Assert.notNull(userId, "can't find account with name ${user.username}")

                    userDetails.get().firstName = user.firstName
                    userDetails.get().lastName = user.lastName
                    userDetails.get().middleName = user.middleName
                    userDetails.get().avatar = user.avatar
                    repository.save(userDetails.get())

                    log.info("account has been updated: " + user.username)

                    status.status = 1
                    return status
                } catch (e: Exception) {
                    log.error("500 Error ${e.message}", e)
                    throw Exception("eeeee")
                }
            } else {
                status.status = 0
                status.message = "can't find account with name ${user.username}"
                return status
            }
        }
        return status
    }

    override fun moveToTrash(id: UUID): Status {
        val status = Status()
        val userDetails = repository.findById(id)
        if (userDetails.isPresent) {
            val removeStatus = authClient.removeToTrashUser(userDetails.get().userId!!)

            if (removeStatus.status == 1) {
                userDetails.get().deletedAt = Timestamp(System.currentTimeMillis())
                repository.save(userDetails.get())

                status.status = 1
                return status
            }

        }
        return status
    }

    override fun delete(id: UUID): Status {
        val status = Status()

        return status
    }

    override fun getUsersBySchool(schoolId: UUID): List<Account> {
        val users = authClient.getUserListBySchoolId(schoolId)
        if (users.isNotEmpty()) {
            val accounts = repository.findAllByUserIdIn(users.map { it.id }.toList() as List<UUID>)
            for (item in accounts) {
                val userFilter = users.filter { it.id == item.userId }
                if (userFilter.isNotEmpty()) {
                    item.roles = userFilter.first().roles
                    item.email = userFilter.first().email
                }
            }
            return accounts
        }
        return listOf()
    }
}
