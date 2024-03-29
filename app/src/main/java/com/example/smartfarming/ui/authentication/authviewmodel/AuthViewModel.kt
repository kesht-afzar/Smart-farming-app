package com.example.smartfarming.ui.authentication.authviewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.example.smartfarming.data.network.Resource
import com.example.smartfarming.data.network.resources.signup.SignUp
import com.example.smartfarming.data.network.resources.signupresponse.SignUpResponse
import com.example.smartfarming.data.network.resources.userSignupResponse.SignupResponse
import com.example.smartfarming.data.repositories.authentication.AuthRepo
import com.example.smartfarming.data.repositories.garden.GardenRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepo: AuthRepo) : ViewModel() {
    val MAX_STEP = 3
    var step = mutableStateOf(0)
    val firstName = MutableLiveData<String>().apply {
        value = ""
    }
    val lastName = MutableLiveData<String>().apply {
        value = ""
    }
    val email = MutableLiveData<String>().apply {
        value = ""
    }
    val phone = mutableStateOf("")

    val passwordWrong = mutableStateOf(false)
    val repeatPasswordWrong = mutableStateOf(false)
    val phoneNumberWrong = mutableStateOf(false)

    private val fullName : String = "${firstName.value} ${lastName.value}"
    private var age = 0
    private var state = ""
    private var city = ""

    val signupResponse = mutableStateOf<Resource<SignUpResponse>?>(value = null)
    private val password = mutableStateOf("")
    private val repeatPassword = mutableStateOf("")

    val waiting = mutableStateOf(false)

    fun checkRepeatPassword(){
        repeatPasswordWrong.value = password.value != repeatPassword.value
    }

    fun checkPhoneNumber(){
        phoneNumberWrong.value = (phone.value.length != 11)
    }

    fun getPhoneNumber() : String {
        return phone.value
    }

    fun setPhoneNumber(value: String) {
        phone.value = value
    }

    fun getRepeatPassword() : String{
        return repeatPassword.value
    }

    fun setRepeatPassword(value : String) {
        repeatPassword.value = value
    }

    fun setPassword(value: String) {
        password.value = value
    }

    fun getPassword() : String {
        return password.value
    }

    fun fieldsValueOk() : Boolean {
        return passwordWrong.value && repeatPasswordWrong.value && phoneNumberWrong.value
    }

    fun checkPassword(){
        val lowerAlphabet = ('a'..'z').joinToString() + ('A' .. 'Z').joinToString()
        val numbers = "0123456789"
        var hasLetters = false
        var hasNumbers = false

        lowerAlphabet.forEach { char ->
            if (char in password.value){
                hasLetters = true
            }
        }

        for (num in numbers){
            if (num in password.value){
                hasNumbers = true
                break
            }
        }
        passwordWrong.value = !(password.value.length > 7 && hasLetters && hasNumbers)
    }

    fun signup(){
        waiting.value = true

        viewModelScope.launch {
            signupResponse.value = authRepo.signup(
                phoneNumber = phone.value,
                password = password.value,
            )

            Log.i("TAG signup1", "${signupResponse.value}")
        }
    }
}

class AuthViewModelFactory(
    val repo : GardenRepo,
    val authRepo: AuthRepo
    ) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(authRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}