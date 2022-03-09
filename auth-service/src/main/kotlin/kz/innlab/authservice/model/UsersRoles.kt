package kz.innlab.authservice.model

import org.hibernate.annotations.GenericGenerator
import java.util.*
import javax.persistence.*

@Entity
@Table(
        name = "users_roles",
        uniqueConstraints= [UniqueConstraint(columnNames = arrayOf("user_id", "role_id"))]
)
class UsersRoles {
        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(
                name = "UUID",
                strategy = "org.hibernate.id.UUIDGenerator"
        )
        var id: UUID? = null

        @Column(name = "USER_ID", nullable = false)
        var userId: UUID? = null

        @Column(name = "ROLE_ID", nullable = false)
        var roleId: UUID? = null

}
