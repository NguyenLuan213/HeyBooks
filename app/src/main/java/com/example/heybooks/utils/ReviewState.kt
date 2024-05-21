package com.example.heybooks.utils

import com.example.books.model.ReviewWithUserInfo
import com.example.heybooks.model.UseData

sealed class ReviewState {
    data class Success(val data: List<ReviewWithUserInfo>) : ReviewState()
    data class Error(val exception: Throwable) : ReviewState()
    object Empty: ReviewState()
    object Loading : ReviewState()
}

