package kz.innlab.authservice.model.payload

import javax.validation.constraints.NotNull

class UserRequest {
    var username: String? = null
    lateinit var password: String
    var providerId: String? = "local"
    var deviceId: String? = null
    var rememberMe: Boolean? = false
    var idToken: String? = null

    constructor() {}

    constructor(username: String?, password: String) {
        this.username = username
        this.password = password
    }

}
