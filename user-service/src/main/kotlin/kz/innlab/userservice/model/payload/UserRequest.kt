package kz.innlab.userservice.model.payload

import java.util.*

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 13.04.2022
 */
class UserRequest {
    var id: UUID? = null
    var schoolId: UUID? = null
    var firstName: String? = null
    var lastName: String? = null
    var middleName: String? = null
    var fio: String? = null
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var avatar: UUID? = null
    var enabled: Boolean? = null
    var roles: ArrayList<String> = arrayListOf()
}
