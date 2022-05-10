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
@TypeDefs(
    TypeDef(
        name = "string-array",
        typeClass = StringArrayType::class
    )
)
@Getter(AccessLevel.PROTECTED)
@Setter(AccessLevel.PROTECTED)
abstract class Auditable : Serializable {
    @CreatedDate
    @Column(
        name="created_at",
        nullable = false,
        updatable = false)
    var createdAt: Timestamp = Timestamp(System.currentTimeMillis())
        private set

    @Type(type = "string-array")
    @Column(
        name = "creator",
        columnDefinition = "character varying(256)[]"
    )
    @CreatedBy
    private var creator: Array<Array<String>> = arrayOf()
//        fun getCreator() = Util.arrayToValueMap(this.creator)
//        fun setCreator(creator: ArrayList<ValueMap>) {
//            this.creator = Util.valueMapToArray(creator, true)
//        }

    @LastModifiedDate
    @Column(name="updated_at")
    var updatedAt: Timestamp? = null

    @Type(type = "string-array")
    @Column(
        name = "editor",
        columnDefinition = "character varying(256)[]"
    )
    @LastModifiedBy
    private var editor: Array<Array<String>> = arrayOf()
//        fun getEditor() = Util.arrayToValueMap(this.creator)
//        fun setEditor(creator: ArrayList<ValueMap>) {
//            this.creator = Util.valueMapToArray(creator, true)
//        }

    @Column(name="deleted_at")
    var deletedAt: Timestamp? = null

    @Column(name = "owner")
    var owner: UUID? = null

}
