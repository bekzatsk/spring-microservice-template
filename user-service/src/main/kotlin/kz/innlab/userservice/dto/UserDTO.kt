package kz.innlab.userservice.dto

//import org.hibernate.validator.constraints.Length
import java.sql.Timestamp
import java.util.*

//import javax.validation.constraints.NotNull

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
class UserDTO {
    var id: UUID? = null
    var schoolId: UUID? = null
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var enabled: Boolean? = null
    var isBlocked: Timestamp? = null
    var roles: ArrayList<String> = arrayListOf()
}
