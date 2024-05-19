package com.example.heybooks.utils

import com.example.books.model.Review
import com.example.heybooks.model.UseData

sealed class ReviewState {
    object Loading : ReviewState()
    data class Success(val reviews: List<Review>) : ReviewState()
    data class Error(val exception: Throwable) : ReviewState()
}

sealed class UserState {
    object Loading : UserState()
    data class Success(val users: List<UseData>) : UserState()
    data class Error(val exception: Throwable) : UserState()
}
