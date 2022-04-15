package kz.innlab.userservice.controller

import kz.innlab.userservice.model.Account
import kz.innlab.userservice.model.User
import kz.innlab.userservice.model.payload.UserRequest
import kz.innlab.userservice.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.security.Principal
import java.util.*
import javax.validation.Valid

@RestController
class AccountController {

    @Autowired
    lateinit var accountService: AccountService

//    @PreAuthorize("#oauth2.hasScope('server') or #name.equals('demo')")
//    @GetMapping("/{name}")
//    fun getAccountByName(@PathVariable name: String): Optional<Account> {
//        println("name = $name")
//        return accountService.findByName(name)
//    }

    @GetMapping("/current")
    fun getCurrentAccount(principal: Principal): Principal {
        println(principal.name)
//        return accountService.findByName(principal.name)
        return principal
    }

    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAccount(principal: Authentication): Authentication {
        println(principal.name)
//        return accountService.findByName(principal.name)
        return principal
    }

    @PutMapping("/current")
    fun saveCurrentAccount(principal: Principal, @RequestBody account: @Valid Account) {
        accountService.saveChanges(principal.name, account)
    }

    @PostMapping("/create")
//    @PreAuthorize("hasRole('ADMIN')")
    fun createNewAccount(@Valid @RequestBody user: UserRequest): Optional<Account> {
        return accountService.create(user)
    }

}
