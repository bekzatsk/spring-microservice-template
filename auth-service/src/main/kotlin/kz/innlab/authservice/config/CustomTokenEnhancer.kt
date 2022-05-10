package kz.innlab.authservice.config

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import kotlin.collections.HashMap


/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 17.04.2022
 */
class CustomTokenEnhancer : TokenEnhancer {
    override fun enhance(accessToken: OAuth2AccessToken, authentication: OAuth2Authentication): OAuth2AccessToken {
        val additionalInfo: MutableMap<String, Any> = HashMap()
//        if (authentication.isAuthenticated) {
//            additionalInfo["user_id"] = (authentication.userAuthentication as UserPrincipal).id
//        }
        (accessToken as DefaultOAuth2AccessToken).additionalInformation = additionalInfo
        return accessToken
    }
}
