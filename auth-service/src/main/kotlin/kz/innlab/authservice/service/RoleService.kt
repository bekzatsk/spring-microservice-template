package kz.innlab.authservice.service

import kz.innlab.authservice.model.Role

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 20.04.2022
 */
interface RoleService {

    fun getList(): ArrayList<Role>

}
