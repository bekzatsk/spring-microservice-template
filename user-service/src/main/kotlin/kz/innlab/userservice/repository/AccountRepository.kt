package kz.innlab.userservice.repository

import kz.innlab.userservice.model.Account
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface AccountRepository: JpaRepository<Account, UUID> {
    fun findByFirstName(firstName: String): Optional<Account>
    fun findByUserId(userId: UUID): Optional<Account>
    fun findAllByUserIdIn(userIds: List<UUID>): List<Account>

    @Query("SELECT ud.* FROM user_details ud " +
            "INNER JOIN users u ON ud.user_id = u.id " +
            "WHERE ud.deleted_at IS NULL AND u.deleted_at IS NULL", nativeQuery = true)
    fun getAccountList(page: Pageable): Page<Account>

    @Query("SELECT ud.* FROM user_details ud " +
            "INNER JOIN users u ON ud.user_id = u.id " +
            "WHERE ud.deleted_at IS NULL AND u.deleted_at IS NULL " +
            "AND LOWER(CONCAT(ud.first_name, ' ', ud.last_name, ' ', ud.middle_name)) LIKE :search", nativeQuery = true)
    fun getAccountListBySearch(search: String, page: Pageable): Page<Account>

    @Query("SELECT ud.* FROM user_details ud " +
            "INNER JOIN users u ON ud.user_id = u.id " +
            "INNER JOIN users_roles ur ON ur.user_id = u.id " +
            "INNER JOIN roles r ON ur.role_id = r.id " +
            "WHERE ud.deleted_at IS NULL AND u.deleted_at IS NULL AND r.deleted_at IS NULL " +
            "AND LOWER(r.name) = LOWER(:role)", nativeQuery = true)
    fun getAccountListByRole(role: String, page: Pageable): Page<Account>

    @Query("SELECT ud.* FROM user_details ud " +
            "INNER JOIN users u ON ud.user_id = u.id " +
            "INNER JOIN users_roles ur ON ur.user_id = u.id " +
            "INNER JOIN roles r ON ur.role_id = r.id " +
            "WHERE ud.deleted_at IS NULL AND u.deleted_at IS NULL AND r.deleted_at IS NULL " +
            "AND LOWER(r.name) = LOWER(:role) " +
            "AND LOWER(CONCAT(ud.first_name, ' ', ud.last_name, ' ', ud.middle_name)) LIKE :search", nativeQuery = true)
    fun getAccountListByRoleAndSearch(role: String, search: String, page: Pageable): Page<Account>
}
