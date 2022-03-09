package kz.innlab.authservice.service.security.jwt

import org.springframework.security.core.GrantedAuthority

class JwtResponse(var accessToken: String?, var refreshToken: String?) {
}
