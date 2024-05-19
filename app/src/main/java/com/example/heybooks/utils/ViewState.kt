package com.example.heybooks.utils
import com.example.books.model.BookItems

sealed class ViewState {
    object Empty: ViewState()
    object Loading : ViewState()
    data class Success(val data: List<BookItems>) : ViewState()
    data class Error(val exception: Throwable) : ViewState()
}