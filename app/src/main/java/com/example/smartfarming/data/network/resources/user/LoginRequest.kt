package com.example.smartfarming.data.network.resources.user

import com.google.gson.Gson

data class LoginRequest(
    val password : String,
    val  phoneNumber : String
)

fun request2JSON(password: String, usernameOrPhoneNumber: String) : String{
    val gson = Gson()
    return gson.toJson(LoginRequest(password = password, phoneNumber = usernameOrPhoneNumber))
}