package kz.innlab.userservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import kz.innlab.userservice.dto.Status
import kz.innlab.userservice.model.Account
import kz.innlab.userservice.model.payload.UserRequest
import kz.innlab.userservice.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*
import javax.validation.Valid


@RestController
class AccountController {

    @Autowired
    lateinit var accountService: AccountService

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    fun getUserList(
        @RequestParam(value = "page") page: Int? = 1,
        @RequestParam(value = "size") size: Int? = 20,
        @RequestParam params: MutableMap<String, String> = mutableMapOf()
    ): Page<Account> {
        var pageR: PageRequest = PageRequest.of((page ?: 1) - 1, (size ?: 20), Sort.by(Sort.Direction.DESC, "created_at"))
        return accountService.getList(pageR, params)
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getAccountById(@PathVariable(value = "id") id: UUID): Optional<UserRequest> {
        return accountService.getAccountById(id)
    }

    @GetMapping("/current")
    fun getCurrentAccount(principal: Principal): Optional<UserRequest> {
        return accountService.findByName(principal.name)
    }

    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAccount(principal: OAuth2Authentication): OAuth2Authentication {
        println(principal.name)
//        return accountService.findByName(principal.name)
        return principal
    }

    @PutMapping("/update")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun saveCurrentAccount(@Valid @RequestBody user: UserRequest): Status {
        return accountService.saveChanges(user)
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    fun createNewAccount(@Valid @RequestBody user: UserRequest): Status {
        return accountService.create(user)
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun removeAccount(@PathVariable(value = "id") id: UUID): Status {
        return accountService.moveToTrash(id)
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun deleteAccount(@PathVariable(value = "id") id: UUID): Status {
        return accountService.delete(id)
    }

    @GetMapping("/list/school/{schoolId}")
    @PreAuthorize("#oauth2.hasScope('server') or hasRole('ADMIN')")
    fun getUserListBySchool(@PathVariable schoolId: UUID): List<Account> {
        return accountService.getUsersBySchool(schoolId)
    }

    @GetMapping("/pre")
    fun getEx(principal: Authentication): Any? {
        val oMapper = ObjectMapper()
        return Optional.ofNullable(SecurityContextHolder.getContext())
            .map(SecurityContext::getAuthentication)
            .filter(Authentication::isAuthenticated)
            .map(Authentication::getDetails)
            .map { r -> oMapper.convertValue(r, MutableMap::class.java) }
            .map { r -> r["tokenValue"] }
    }


}
