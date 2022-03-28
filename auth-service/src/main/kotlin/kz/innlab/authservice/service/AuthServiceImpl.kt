package kz.innlab.authservice.service

import kz.innlab.authservice.model.User
import kz.innlab.authservice.model.payload.JwtAuthenticationResponse
import kz.innlab.authservice.model.payload.UserRequest
import kz.innlab.authservice.model.token.RefreshToken
import kz.innlab.authservice.repository.UserRepository
import kz.innlab.authservice.service.security.jwt.JwtProvider
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@Service
class AuthServiceImpl: AuthService {
    private val logger = LoggerFactory.getLogger(javaClass)

    private val encoder = BCryptPasswordEncoder()

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var tokenProvider: JwtProvider

    @Autowired
    lateinit var refreshTokenService: RefreshTokenService

    @Autowired
    lateinit var repository: UserRepository

    override fun authenticate(loginRequest: UserRequest, withPassword: Boolean): MutableMap<String, Any?> {
        val status: MutableMap<String, Any?> = mutableMapOf()
        var userCandidate = repository.findByUsernameIgnoreCaseAndDeletedAtIsNull(loginRequest.username!!)
        if (userCandidate.isEmpty) {
            userCandidate = repository.findByEmailIgnoreCaseAndDeletedAtIsNull(loginRequest.username!!)
        } else {
            // Find by Phone Number
        }
//        if(userCandidate.isEmpty || userCandidate.get().getEnabled() != true) {
        if(userCandidate.isEmpty || userCandidate.get().enabled != true) {
            status["message"] = "User not found or password wrong!"
            logger.info("User not found or password wrong! User is empty! User: ${loginRequest.username}")
            return status
        }
        try {
            if(userCandidate.get().isBlocked != null && userCandidate.get().isBlocked!!.time + 1800000 - System.currentTimeMillis() > 0){
                throw RuntimeException()
            }
            loginRequest.username = userCandidate.get().username
            val authentication: Optional<Authentication> = authenticateUser(loginRequest, withPassword)
            SecurityContextHolder.getContext().authentication = authentication.get()

            userCandidate.get().loginAttempts = 0
            userCandidate.get().isBlocked = null
            repository.save(userCandidate.get())
            val refreshTokenOptional = createAndPersistRefreshToken(loginRequest)
            if (refreshTokenOptional.isEmpty) {
                status["message"] = "Couldn't create refresh token for: ${loginRequest}"
                logger.info("Couldn't create refresh token for: ${loginRequest.username}")
                return status
            }
            val refreshToken = refreshTokenOptional.get().token
            val jwtToken: String = generateToken(authentication.get().name)
            status["status"] = 1
            status["message"] = "OK"
            status["value"] = JwtAuthenticationResponse(jwtToken, refreshToken, tokenProvider.getExpiryDuration())
            return status
        } catch (e: Exception) {
            if(userCandidate.get().loginAttempts!! >= 5){
                if(userCandidate.get().isBlocked == null) {
                    userCandidate.get().isBlocked = Timestamp(System.currentTimeMillis())
                }
                val blockedTimeMs: Long = userCandidate.get().isBlocked!!.time
                if (blockedTimeMs + 1800000 - System.currentTimeMillis() < 0) {
                    userCandidate.get().loginAttempts = 1
                    userCandidate.get().isBlocked = null
                    repository.save(userCandidate.get())
                    status["message"] = "User not found or password wrong!"
                    logger.info("User not found or password wrong! User is blocked! User: ${loginRequest.username}")
                    return status
                }

                val leftMinutes: Long = ((blockedTimeMs + 1800000 - System.currentTimeMillis()) / 1000) / 60
                val leftSeconds: Long = ((blockedTimeMs + 1800000 - System.currentTimeMillis()) / 1000) % 60

                repository.save(userCandidate.get())
                status["message"] = "Your account will unblocked after $leftMinutes minutes and $leftSeconds seconds!"
                return status
            }
            userCandidate.get().incrementLoginAttempts()
            repository.save(userCandidate.get())
            status["message"] = "User not found or password wrong!"
            logger.info("User not found or password wrong! = User: ${loginRequest.username}")
            logger.info(e.message)
            return status
        }

    }

    fun authenticateUser(loginRequest: UserRequest, withPassword: Boolean = true): Optional<Authentication> {
        if (!withPassword) {
            return Optional.ofNullable(
                UsernamePasswordAuthenticationToken(
                    loginRequest.username,
                    null,
                    AuthorityUtils.createAuthorityList()
                )
            )
        }
        return Optional.ofNullable(authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.username,
                loginRequest.password
            )
        ))

    }

    fun createAndPersistRefreshToken(loginRequest: UserRequest): Optional<RefreshToken> {
        var currentUser = repository.findByUsernameAndDeletedAtIsNull(loginRequest.username!!)

        var refreshToken: RefreshToken = refreshTokenService.createRefreshToken()
        refreshToken.userId = currentUser.get().id!!
        refreshToken = refreshTokenService.save(refreshToken)
        return Optional.ofNullable<RefreshToken?>(refreshToken)
    }

    fun generateToken(username: String): String {
        return tokenProvider.generateAccessJwtToken(username)
    }
}
