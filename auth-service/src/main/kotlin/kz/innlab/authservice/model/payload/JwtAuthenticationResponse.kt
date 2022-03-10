package kz.innlab.authservice.model.payload

import java.util.*

class JwtAuthenticationResponse {
    var accessToken: String
    var refreshToken: UUID
    var tokenType: String
    var expiryDuration: Long

    constructor(accessToken: String, refreshToken: UUID, expiryDuration: Long) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.expiryDuration = expiryDuration
        tokenType = "Bearer "
    }

}
