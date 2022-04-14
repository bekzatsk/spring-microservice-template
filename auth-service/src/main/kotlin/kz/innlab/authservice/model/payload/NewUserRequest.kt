package kz.innlab.authservice.model.payload

import java.sql.Timestamp
import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 13.04.2022
 */
class NewUserRequest {
    var id: UUID? = null
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var enabled: Boolean? = null
    var isBlocked: Timestamp? = null
    var roles: ArrayList<String> = arrayListOf()
}
