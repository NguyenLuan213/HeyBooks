package com.example.heybooks.utils

import com.example.books.model.BookDetailsViewState

sealed class DetailViewStateTab {
    object Empty: DetailViewStateTab()
    object Loading : DetailViewStateTab()
    data class Success(val data: BookDetailsViewState) : DetailViewStateTab()
    data class Error(val exception: Throwable) : DetailViewStateTab()
}