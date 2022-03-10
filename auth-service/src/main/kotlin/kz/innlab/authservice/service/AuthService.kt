package kz.innlab.authservice.service

import kz.innlab.authservice.model.payload.UserRequest

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 09.03.2022
 */
interface AuthService {

    fun authenticate(loginRequest: UserRequest, withPassword: Boolean = true): MutableMap<String, Any?>

}
