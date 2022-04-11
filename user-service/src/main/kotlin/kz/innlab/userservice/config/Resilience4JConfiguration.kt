package kz.innlab.userservice.config

import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration


/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.04.2022
 */
@Configuration
class Resilience4JConfiguration {
    @Bean
    fun globalCustomConfiguration(): Customizer<Resilience4JCircuitBreakerFactory> {
        val circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50f)
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .slidingWindowSize(2)
            .build()
        val timeLimiterConfig = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(4))
            .build()
        // the circuitBreakerConfig and timeLimiterConfig objects
        return Customizer<Resilience4JCircuitBreakerFactory> { factory ->
            factory.configureDefault { id ->
                Resilience4JConfigBuilder(id)
                    .timeLimiterConfig(timeLimiterConfig)
                    .circuitBreakerConfig(circuitBreakerConfig)
                    .build()
            }
        }
    }

    @Bean
    fun specificCustomConfiguration1(): Customizer<Resilience4JCircuitBreakerFactory>? {
        val circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50f)
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .slidingWindowSize(2)
            .build()
        val timeLimiterConfig = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(3600))
            .build()

        // the circuitBreakerConfig and timeLimiterConfig objects
        return Customizer { factory: Resilience4JCircuitBreakerFactory ->
            factory.configure(
                { builder: Resilience4JConfigBuilder ->
                    builder.circuitBreakerConfig(circuitBreakerConfig)
                        .timeLimiterConfig(timeLimiterConfig).build()
                }, "mailservice-circuit-breaker"
            )
        }
    }

    @Bean
    fun mediaCustomConfiguration(): Customizer<Resilience4JCircuitBreakerFactory>? {
        val circuitBreakerConfig = CircuitBreakerConfig.custom()
            .failureRateThreshold(50f)
            .waitDurationInOpenState(Duration.ofMillis(1000))
            .slidingWindowSize(2)
            .build()
        val timeLimiterConfig = TimeLimiterConfig.custom()
            .timeoutDuration(Duration.ofSeconds(600))
            .build()

        // the circuitBreakerConfig and timeLimiterConfig objects
        return Customizer { factory: Resilience4JCircuitBreakerFactory ->
            factory.configure(
                { builder: Resilience4JConfigBuilder ->
                    builder.circuitBreakerConfig(circuitBreakerConfig)
                        .timeLimiterConfig(timeLimiterConfig).build()
                }, "media-circuit-breaker"
            )
        }
    }
}
