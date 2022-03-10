package kz.innlab.userservice.model

import org.hibernate.validator.constraints.Length
import java.util.*
import javax.validation.constraints.NotNull

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
class User {

    var id: @NotNull UUID? = null
    var username: @NotNull @Length(min = 3, max = 20) String? = null
    var password: @NotNull @Length(min = 6, max = 40) String? = null

}
