package com.example.heybooks.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.MainViewModel

sealed class LoginViewState {
    object Empty: LoginViewState()
    object Loading : LoginViewState()
    data class Success(val idnd: Int) : LoginViewState()
    data class Error(val exception: Throwable) : LoginViewState()
}
@Composable
fun CheckSignedIn(viewModel: MainViewModel, actions: MainActions) {
    val alreadySignIn = remember { mutableStateOf(false) }
    val signIn = viewModel.signIn.value
    if (signIn && !alreadySignIn.value) {
        alreadySignIn.value = true
        actions.gotoBookList()
    }
}


