package kz.innlab.authservice.repository

import kz.innlab.authservice.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*
import kotlin.collections.ArrayList

interface UserRepository: JpaRepository<User, UUID> {
    fun findByIdAndDeletedAtIsNull(id: UUID): Optional<User>

    fun findAllByDeletedAtIsNull(): ArrayList<User>
    fun findAllByDeletedAtIsNull(page: Pageable): Page<User>
    fun findAllByIdInAndDeletedAtIsNull(@Param("id") ids: List<UUID>, page: Pageable): Page<User>
    fun findAllByIdInAndDeletedAtIsNull(@Param("id") ids: List<UUID>): ArrayList<User>
    fun findAllBySchoolIdAndDeletedAtIsNull(schoolId: UUID): List<User>

    fun findByUsernameAndDeletedAtIsNull(@Param("username") username: String): Optional<User>
    fun findByUsernameIgnoreCaseAndDeletedAtIsNull(@Param("username") username: String): Optional<User>

    fun findByUsernameOrEmail(@Param("username") username: String, @Param("email") email: String): ArrayList<User>
    fun findByUsernameIgnoreCase(@Param("username") username: String): Optional<User>
    fun findByUsernameIgnoreCaseOrEmailIgnoreCase(@Param("username") username: String, @Param("email") email: String): ArrayList<User>

    fun findByEmailAndDeletedAtIsNull(@Param("email") email: String): Optional<User>
    fun findByEmailIgnoreCaseAndDeletedAtIsNull(@Param("email") email: String): Optional<User>
    fun findByEmailIgnoreCase(@Param("email") email: String): Optional<User>

    fun findAllBySchoolId(@Param("schoolId") schoolId: UUID): ArrayList<User>

    @Query("SELECT users.* " +
            "FROM roles " +
            "RIGHT JOIN users_roles " +
            "ON roles.id = users_roles.role_id " +
            "RIGHT JOIN users " +
            "ON users_roles.user_id = users.id " +
            "WHERE priority_number BETWEEN :fromPriority AND :toPriority " +
            "AND roles.deleted_at IS NULL AND users.deleted_at IS NULL", nativeQuery = true)
    fun managerList(@Param("fromPriority") fromPriority: Long?, @Param("toPriority") toPriority: Long?): ArrayList<User>
}
