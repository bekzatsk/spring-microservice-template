package kz.innlab.authservice.model.token

import kz.innlab.authservice.model.Auditable
import org.hibernate.annotations.GenericGenerator
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "REFRESH_TOKEN")
class RefreshToken {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "ID", updatable = false)
    var id: UUID? = null

    @Column(name = "TOKEN", nullable = false, unique = true)
    lateinit var token: UUID

    @Column(name = "USER_ID")
    var userId: UUID? = null

    @Column(name = "REFRESH_COUNT")
    var refreshCount: Long? = null

    @Column(name = "EXPIRY_DT", nullable = false)
    lateinit var  expiryDate: Instant

    @Column(name = "FROM_USED_TOKEN")
    lateinit var  fromUsedToken: UUID

    constructor() {}

    constructor(id: UUID?, token: UUID, userId: UUID?, refreshCount: Long?, expiryDate: Instant) {
        this.id = id
        this.token = token
        this.userId = userId
        this.refreshCount = refreshCount
        this.expiryDate = expiryDate
    }

    fun incrementRefreshCount() { this.refreshCount = this.refreshCount!! + 1 }
}
