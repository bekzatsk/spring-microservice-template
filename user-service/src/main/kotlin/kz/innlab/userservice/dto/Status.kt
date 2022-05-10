package kz.innlab.userservice.dto

data class Status (
    var status: Int = 0,
    var message: String? = "Something went wrong please try again!",
    var value: Any? = null
)
