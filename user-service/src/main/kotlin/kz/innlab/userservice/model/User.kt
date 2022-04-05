package kz.innlab.userservice.model

//import org.hibernate.validator.constraints.Length
import org.hibernate.annotations.GenericGenerator
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
    var username: String? = null
    var password: String? = null
}
