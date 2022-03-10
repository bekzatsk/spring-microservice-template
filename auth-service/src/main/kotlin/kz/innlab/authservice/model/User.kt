package kz.innlab.authservice.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vladmihalcea.hibernate.type.array.StringArrayType
import com.vladmihalcea.hibernate.type.array.UUIDArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
//import kz.innlab.edm.reference.model.type.ValueMap
//import kz.innlab.edm.user.service.UserService
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
@JsonIgnoreProperties(value = ["password", "path", "rolesCollection"], allowGetters = false)
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

    @Column(name = "PHONE", unique = true)
    var phone: String = ""
        set(value) {
            field = value.trim()
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


    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH])
    @JoinTable(name = "USERS_ROLES",
        joinColumns = [JoinColumn(name = "USER_ID", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "ROLE_ID", referencedColumnName = "id")])
    var rolesCollection: Collection<Role> = listOf()

//    @Transient
//    var roles: Collection<ValueMap> = listOf()
//        get() {
//            field = listOf()
//            for (item in rolesCollection) {
//                if (item.deletedAt == null) {
//                    field = field.plus(ValueMap(item.id!!, item.name))
//                }
//            }
//            return field
//        }
//        private set

    // ================================================================================
    fun markVerificationConfirmed() { emailVerified = true }
    fun incrementLoginAttempts() { loginAttempts = loginAttempts!! + 1 }

    fun addRole(roles: Collection<Role>) {
        for (item in roles) {
            if (!rolesCollection.contains(item)) {
                rolesCollection = rolesCollection.plus(item)
            }
        }
    }

}
