package com.example.heybooks.utils
import com.example.heybooks.model.UseData


sealed class LoginViewState {
    object Empty: LoginViewState()
//    object Loading : LoginViewState()
    data class Success(val data: List<UseData>) : LoginViewState()
    data class Error(val exception: Throwable) : LoginViewState()
}



