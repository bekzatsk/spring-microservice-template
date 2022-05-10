package kz.innlab.fileservice.config.custom

import org.aopalliance.intercept.MethodInvocation
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.provider.expression.OAuth2ExpressionParser
import org.springframework.security.oauth2.provider.expression.OAuth2SecurityExpressionMethods

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 05.04.2022
 */
class OAuth2MethodSecurityExpressionHandler : DefaultMethodSecurityExpressionHandler() {
    init {
        expressionParser = OAuth2ExpressionParser(expressionParser)
    }

    override fun createEvaluationContextInternal(
        authentication: Authentication?,
        mi: MethodInvocation?
    ): StandardEvaluationContext {
        val ec = super.createEvaluationContextInternal(authentication, mi)
        val methods = OAuth2SecurityExpressionMethods(authentication)
        ec.setVariable("oauth", methods)
        ec.setVariable("oauth2", methods)
        return ec
    }
}
