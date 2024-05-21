package com.example.heybooks.utils

import com.example.books.model.ReviewWithUserInfo
import com.example.heybooks.model.UseData

sealed class UsersViewState {
    data class Success(val data: List<UseData>) : UsersViewState()
    data class Error(val exception: Throwable) : UsersViewState()
    object Empty: UsersViewState()
    object Loading : UsersViewState()
}