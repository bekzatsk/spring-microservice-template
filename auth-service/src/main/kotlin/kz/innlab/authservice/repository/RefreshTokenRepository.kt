package kz.innlab.authservice.repository

import kz.innlab.authservice.model.token.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*
import javax.transaction.Transactional

interface RefreshTokenRepository: JpaRepository<RefreshToken, UUID> {

    fun findByToken(token: UUID): Optional<RefreshToken>

    @Transactional
    fun deleteByToken(@Param("token") token: UUID)

    @Transactional
    fun deleteAllByExpiryDateLessThan(@Param("expiry_dt") expiry_dt: Instant)
}
