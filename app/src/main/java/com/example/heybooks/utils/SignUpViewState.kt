package com.example.heybooks.utils

import com.example.books.model.BookItem

sealed class SignUpViewState {
    object Empty: SignUpViewState()
    object Loading : SignUpViewState()
    data class Success(val idnd: Int) : SignUpViewState()
    data class Error(val exception: Throwable) : SignUpViewState()
}