package kz.innlab.authservice.service

import kz.innlab.authservice.model.Role
import kz.innlab.authservice.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

/**
 * @project microservice-template
 * @author Bekzat Sailaubayev on 20.04.2022
 */
@Service
class RoleServiceImpl: RoleService {

    @Autowired
    lateinit var repository: RoleRepository

    override fun getList(): ArrayList<Role> {
        return repository.findAllByDeletedAtIsNull(Sort.by(Sort.Direction.DESC, "priorityNumber"))
    }
}
