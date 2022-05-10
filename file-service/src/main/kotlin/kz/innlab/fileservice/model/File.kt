package kz.innlab.fileservice.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*
import javax.persistence.*

/**
 * @project edm-spring
 * @author Bekzat Sailaubayev on 12.02.2022
 */

@Entity
@Table(
    name = "FILES"
)
@JsonIgnoreProperties(value = ["hashCode"], allowGetters = false, ignoreUnknown = true)
class File: Auditable<String?>()  {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null

    @Column(name = "FILE_NAME")
    var fileName: String = ""

    @Column(name = "FORMAT")
    var fileFormat: String? = null

    @Column(name = "MIME")
    var mime: String? = null

    @Column(name = "SIZE")
    var size: Long? = null

    @Column(name = "HASH_CODE")
    var hashCode: String = ""

    @Column(name = "MODULE")
    var module: String? = null

    @Column(name = "SUBJECT_ID")
    var subjectId: UUID? = null

    @Column(name = "OBJECT_ID")
    var objectId: UUID? = null

}
