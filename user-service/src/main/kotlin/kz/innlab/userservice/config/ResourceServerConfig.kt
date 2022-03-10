package kz.innlab.userservice.config

import feign.RequestInterceptor
import kz.innlab.userservice.service.security.CustomUserInfoTokenServices
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.openfeign.security.OAuth2FeignRequestInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext
import org.springframework.security.oauth2.client.OAuth2RestTemplate
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@Configuration
@EnableResourceServer
class ResourceServerConfig @Autowired constructor(sso: ResourceServerProperties) :
    ResourceServerConfigurerAdapter() {
    private val sso: ResourceServerProperties

    init {
        this.sso = sso
    }

    @Bean
    @ConfigurationProperties(prefix = "security.oauth2.client")
    fun clientCredentialsResourceDetails(): ClientCredentialsResourceDetails {
        return ClientCredentialsResourceDetails()
    }

    @Bean
    fun oauth2FeignRequestInterceptor(): RequestInterceptor {
        return OAuth2FeignRequestInterceptor(DefaultOAuth2ClientContext(), clientCredentialsResourceDetails())
    }

    @Bean
    fun clientCredentialsRestTemplate(): OAuth2RestTemplate {
        return OAuth2RestTemplate(clientCredentialsResourceDetails())
    }

    @Bean
    fun tokenServices(): ResourceServerTokenServices {
        return CustomUserInfoTokenServices(sso.userInfoUri, sso.clientId)
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers("/", "/demo").permitAll()
            .anyRequest().authenticated()
    }
}

