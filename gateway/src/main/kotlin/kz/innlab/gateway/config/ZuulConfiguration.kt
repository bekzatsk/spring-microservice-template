package kz.innlab.gateway.config

import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.boot.web.servlet.error.ErrorController
import org.springframework.cglib.proxy.*
import org.springframework.cloud.netflix.zuul.filters.RouteLocator
import org.springframework.cloud.netflix.zuul.web.ZuulController
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.reflect.Constructor
import java.lang.reflect.Method


/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
@Configuration
class ZuulConfiguration {
    /**
     * Constructs a new bean post-processor for Zuul.
     *
     * @param routeLocator    the route locator.
     * @param zuulController  the Zuul controller.
     * @param errorController the error controller.
     * @return the new bean post-processor.
     */
    @Bean
    fun zuulPostProcessor(
        @Autowired routeLocator: RouteLocator?,
        @Autowired zuulController: ZuulController?,
        @Autowired(required = false) errorController: ErrorController?
    ): ZuulPostProcessor {
        return ZuulPostProcessor(routeLocator, zuulController, errorController)
    }

    private enum class LookupHandlerCallbackFilter : CallbackFilter {
        INSTANCE;

        override fun accept(method: Method): Int {
            return if (METHOD == method.getName()) {
                0
            } else 1
        }
    }

    private enum class LookupHandlerMethodInterceptor : MethodInterceptor {
        INSTANCE;

        @Throws(Throwable::class)
        override fun intercept(target: Any?, method: Method?, args: Array<Any>, methodProxy: MethodProxy): Any? {
            return if (ERROR_PATH == args[0]) {
                // by entering this branch we avoid the ZuulHandlerMapping.lookupHandler method to trigger the
                // NoSuchMethodError
                null
            } else methodProxy.invokeSuper(target, args)
        }
    }

    class ZuulPostProcessor internal constructor(
        private val routeLocator: RouteLocator?,
        private val zuulController: ZuulController?,
        errorController: ErrorController?
    ) :
        BeanPostProcessor {
        private val hasErrorController: Boolean

        init {
            hasErrorController = errorController != null
        }

        @Throws(BeansException::class)
        override fun postProcessAfterInitialization(bean: Any, beanName: String): Any? {
            if (hasErrorController && bean is ZuulHandlerMapping) {
                val enhancer = Enhancer()
                enhancer.setSuperclass(ZuulHandlerMapping::class.java)
                enhancer.setCallbackFilter(LookupHandlerCallbackFilter.INSTANCE) // only for lookupHandler
                enhancer.setCallbacks(arrayOf(LookupHandlerMethodInterceptor.INSTANCE, NoOp.INSTANCE))
                val ctor: Constructor<*> = ZuulHandlerMapping::class.java.constructors[0]
                return enhancer.create(ctor.getParameterTypes(), arrayOf(routeLocator, zuulController))
            }
            return bean
        }
    }

    companion object {
        /**
         * The path returned by ErrorController.getErrorPath() with Spring Boot < 2.5
         * (and no longer available on Spring Boot >= 2.5).
         */
        private const val ERROR_PATH = "/error"
        private const val METHOD = "lookupHandler"
    }
}
