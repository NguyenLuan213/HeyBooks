package com.example.heybooks.utils

import com.example.books.model.BookItems

sealed class DetailViewState {
    object Empty: DetailViewState()
    object Loading : DetailViewState()
    data class Success(val data: BookItems) : DetailViewState()
    data class Error(val exception: Throwable) : DetailViewState()
}