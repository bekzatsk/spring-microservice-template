package kz.innlab.authservice.repository

import kz.innlab.authservice.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.Param
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 13.04.2022
 */
interface RoleRepository: JpaRepository<Role, UUID> {
    fun findByNameIgnoreCaseAndDeletedAtIsNull(@Param("name") name: String): Optional<Role>

}
