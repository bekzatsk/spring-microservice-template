package kz.innlab.authservice.service.security

import kz.innlab.authservice.model.UserPrincipal
import kz.innlab.authservice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import java.util.stream.Collectors

@Service
class UserDetailsServiceImpl: UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsernameIgnoreCaseAndDeletedAtIsNull(username).get()

        val authorities: List<GrantedAuthority> = user.rolesCollection.stream().map { role -> SimpleGrantedAuthority("ROLE_${role.name}") }
            .collect(Collectors.toList<GrantedAuthority>())

        val userPrincipal = org.springframework.security.core.userdetails.User
            .withUsername(username)
            .password(user.password)
            .authorities(authorities)
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .disabled(false)
            .build()

        return UserPrincipal(userPrincipal, user.id!!)
    }
}
