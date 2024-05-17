package com.example.heybooks.utils

import com.example.books.model.BookItem

sealed class DetailViewState {
    object Empty: DetailViewState()
    object Loading : DetailViewState()
    data class Success(val data: BookItem) : DetailViewState()
    data class Error(val exception: Throwable) : DetailViewState()
}