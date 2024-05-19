package com.example.books.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class BookItems(
    val isbn: String = "",
    val title: String = "",
    val authors: String = "",
    val categories: String = "",
    val bookintroduction:  String = "",
    val bookcontent:  String = "",
    val imageUrl: String = "",
    val userId: String = "",
    val reviewId: String = ""

)
@Serializable
data class Review(
    val reviewId: String = "",
    val isbn: String = "",
    val userId: String = "",
    val comment: String = ""
)


@Serializable
data class BookDetailsViewState(
    val summary: String,
    val content: String,
    val reviews: List<Review>
)