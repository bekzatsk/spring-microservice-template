package kz.innlab.fileservice.config

import kz.innlab.fileservice.service.AuditorAwareImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 17.04.2022
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
class JpaConfig {
    @Bean
    fun auditorAware(): AuditorAware<String?> {
        return AuditorAwareImpl()
    }
}
