package kz.innlab.authservice.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore


/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 28.03.2022
 */
@Configuration
@EnableAuthorizationServer
class OAuth2AuthorizationConfig : AuthorizationServerConfigurerAdapter() {
    private val tokenStore: TokenStore = InMemoryTokenStore()
    private val NOOP_PASSWORD_ENCODE = "{noop}"

    @Autowired
    @Qualifier("authenticationManagerBean")
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private val env: Environment? = null

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {

        // TODO persist clients details

        // @formatter:off
        clients.inMemory()
            .withClient("browser")
            .secret("password")
            .authorizedGrantTypes("refresh_token", "password")
            .scopes("ui")
            .and()
            .withClient("user-service")
            .secret("password")
            .authorizedGrantTypes("client_credentials", "refresh_token")
            .scopes("server")
            .and()
            .withClient("statistics-service")
            .secret("password")
            .authorizedGrantTypes("client_credentials", "refresh_token")
            .scopes("server")
            .and()
            .withClient("notification-service")
            .secret("password")
            .authorizedGrantTypes("client_credentials", "refresh_token")
            .scopes("server")
        // @formatter:on
    }

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints
            .tokenStore(tokenStore)
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService)
    }

    @Throws(Exception::class)
    override fun configure(oauthServer: AuthorizationServerSecurityConfigurer) {
        oauthServer
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
    }
}
