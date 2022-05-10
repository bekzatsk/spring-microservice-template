package kz.innlab.authservice.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vladmihalcea.hibernate.type.array.StringArrayType
import lombok.AccessLevel
import lombok.Getter
import lombok.Setter
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.hibernate.annotations.TypeDefs
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import java.io.Serializable
import java.sql.Timestamp
import java.util.*
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(value = ["deletedAt"], allowGetters = false)
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
abstract class Auditable<U> : Serializable {
    @CreatedDate
    var createdAt: Timestamp = Timestamp(System.currentTimeMillis())
        private set

    @CreatedBy
    protected var creator: U? = null

    @LastModifiedDate
    @Column(name="updated_at")
    var updatedAt: Timestamp? = null

    @LastModifiedBy
    private var editor: U? = null

    @Column(name="deleted_at")
    var deletedAt: Timestamp? = null

}
