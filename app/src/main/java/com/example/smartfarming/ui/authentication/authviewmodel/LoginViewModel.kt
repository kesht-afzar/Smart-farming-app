package com.example.smartfarming.ui.authentication.authviewmodel

import androidx.lifecycle.*
import com.example.smartfarming.data.UserPreferences
import com.example.smartfarming.data.network.Resource
import com.example.smartfarming.data.network.resources.user.LoginResponse
import com.example.smartfarming.data.repositories.authentication.AuthRepo
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class LoginViewModel(
    private val authRepo: AuthRepo,
    private val userPreferences: UserPreferences
) : ViewModel() {
    val phoneNumber = MutableLiveData<String>().apply {
        value = ""
    }

    val password = MutableLiveData<String>().apply {
        value = ""
    }


    // LOGIN FUNCTION
    val loginResponse : MutableLiveData<Resource<LoginResponse>> = MutableLiveData()

    fun login(){
        viewModelScope.launch {
            loginResponse.value =authRepo.login(password = password.value!!, email = phoneNumber.value!!)
            if (loginResponse.value is Resource.Success){
                userPreferences.saveAuthToken((loginResponse.value as Resource.Success<LoginResponse>).value.response.token)
            }
        }
    }
}

class LoginViewModelFactory(
    val authRepo: AuthRepo,
    val userPreferences: UserPreferences
) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(authRepo, userPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}