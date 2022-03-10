package kz.innlab.authservice.service

import kz.innlab.authservice.model.token.RefreshToken
import kz.innlab.authservice.repository.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class RefreshTokenService {

    @Value("\${app.token.refresh.duration}")
    private var refreshTokenDurationMs: Long = 0

    @Autowired
    lateinit var refreshTokenRepository: RefreshTokenRepository

    fun findByToken(token: UUID): Optional<RefreshToken> { return refreshTokenRepository.findByToken(token)}

    fun save(refreshToken: RefreshToken): RefreshToken { return refreshTokenRepository.save(refreshToken) }

    fun createRefreshToken(): RefreshToken {
        val refreshToken = RefreshToken()
        refreshToken.expiryDate = Instant.now().plusMillis(refreshTokenDurationMs)
        refreshToken.token = UUID.randomUUID()
        refreshToken.refreshCount = 0L
        return refreshToken
    }

    fun verifyExpiration(token: RefreshToken): Boolean {
        println("expired")
        println(token.expiryDate.compareTo(Instant.now()))
        if (token.expiryDate.compareTo(Instant.now()) < 0) {
//            throw TokenRefreshException(token.getToken(), "Expired token. Please issue a new request")
            return false
        }
        return true
    }

    fun deleteById(id: UUID) { refreshTokenRepository.deleteById(id) }

    fun increaseCount(refreshToken: RefreshToken) {
        refreshToken.incrementRefreshCount()
        save(refreshToken)
    }
}
