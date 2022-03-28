//package kz.innlab.authservice.config
//
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.Primary
//import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
//import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices
//import org.springframework.security.oauth2.provider.token.TokenStore
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
//
//
///**
// * @project microservice-template
// * @author Bekzat Sailaubayev on 28.03.2022
// */
//@Configuration
//@EnableAuthorizationServer
//class OAuth2AuthorizationServerConfig : AuthorizationServerConfigurerAdapter() {
//    @Throws(Exception::class)
//    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
//        endpoints.tokenStore(tokenStore())
//            .accessTokenConverter(accessTokenConverter())
//            .authenticationManager(authenticationManager)
//    }
//
//    @Bean
//    fun tokenStore(): TokenStore {
//        return JwtTokenStore(accessTokenConverter())
//    }
//
//    @Bean
//    fun accessTokenConverter(): JwtAccessTokenConverter {
//        val converter = JwtAccessTokenConverter()
//        converter.setSigningKey("123")
//        return converter
//    }
//
//    @Bean
//    @Primary
//    fun tokenServices(): DefaultTokenServices {
//        val defaultTokenServices = DefaultTokenServices()
//        defaultTokenServices.setTokenStore(tokenStore())
//        defaultTokenServices.setSupportRefreshToken(true)
//        return defaultTokenServices
//    }
//}
