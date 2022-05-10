package kz.innlab.authservice.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vladmihalcea.hibernate.type.array.StringArrayType
import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import org.hibernate.annotations.*
import java.sql.Timestamp
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.Table
import kotlin.jvm.Transient


@Entity(name = "USERS")
@Table(name = "USERS")
@JsonIgnoreProperties(value = ["password", "path", "rolesCollection"], allowGetters = false, allowSetters = true)
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    ),
    TypeDef(
        name = "uuid-array",
        typeClass = UUIDArrayType::class
    ),
    TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType::class
    )
)
class User(): Auditable() {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    var id: UUID? = null

    @Column(name = "USERNAME", unique = true, nullable = false)
    var username: String = ""
        set(value) {
//            if (UserService.isUsernameValid(value.lowercase())) {
                field = value.lowercase().trim()
//            }
        }

    @Column(name = "EMAIL", unique = true, nullable = false)
    var email: String = ""
        set(value) {
//            if (UserService.isEmailValid(value.lowercase())) {
                field = value.lowercase().trim()
//            }
        }

    @Column(name = "PASSWORD", columnDefinition = "character varying", nullable = false)
    var password: String = ""

    @Column(name = "ENABLED")
    var enabled: Boolean? = true

    @Type(type = "string-array")
    @Column(
        name = "PATH",
        columnDefinition = "character varying(256)[]"
    )
    var path: Array<Array<String>>? = arrayOf()
        set(value) {
            field = arrayOf()
            if (value != null) {
                field = value
            }
        }

    @Column(name = "EMAIL_VERIFIED")
    var emailVerified: Boolean? = null

    @Column(name = "LOGIN_ATTEMPTS")
    var loginAttempts: Int? = 0
        get() {
            return if (field != null)
                field
            else 0
        }

    @Column(name = "IS_BLOCKED")
    var isBlocked: Timestamp? = null

    @Column(name = "SCHOOL_ID")
    var schoolId: UUID? = null

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH])
    @JoinTable(name = "USERS_ROLES",
        joinColumns = [JoinColumn(name = "USER_ID", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "ROLE_ID", referencedColumnName = "id")])
    var rolesCollection: Collection<Role> = listOf()

    @Transient
    var roles: List<String> = arrayListOf()
        get() {
            field = arrayListOf()
            for (item in rolesCollection) {
                field = field.plus(item.name)
            }
            return field
        }
        private set

    // ================================================================================
    fun markVerificationConfirmed() { emailVerified = true }
    fun incrementLoginAttempts() { loginAttempts = loginAttempts!! + 1 }

}
