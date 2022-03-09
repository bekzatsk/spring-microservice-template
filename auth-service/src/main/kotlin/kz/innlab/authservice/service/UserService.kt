package kz.innlab.authservice.service

import kz.innlab.authservice.model.User

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface UserService {

    fun create(user: User)

}
