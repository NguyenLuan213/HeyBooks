package com.example.heybooks.utils

import com.example.books.model.BookItems

sealed class SavedBooksState {
    object Empty: SavedBooksState()
    object Loading : SavedBooksState()
    data class Success(val data: List<BookItems>) : SavedBooksState()
    data class Error(val exception: Throwable) : SavedBooksState()
}