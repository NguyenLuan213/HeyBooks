package com.example.books.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class BookItem(
    val authors: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val isbn: String = "",
    val longDescription: String = "",
    val pageCount: Int = 0,
    val shortDescription: String = "",
    val status: String = "",
    val thumbnailUrl: String = "",
    val title: String = ""
)