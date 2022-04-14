package kz.innlab.userservice.model

//import org.hibernate.validator.constraints.Length
import org.hibernate.annotations.GenericGenerator
import java.sql.Timestamp
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

//import javax.validation.constraints.NotNull

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
class User {
    var id: UUID? = null
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var enabled: Boolean? = null
    var isBlocked: Timestamp? = null
    var roles: ArrayList<String> = arrayListOf()
}
