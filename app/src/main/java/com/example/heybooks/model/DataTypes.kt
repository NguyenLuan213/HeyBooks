package com.example.heybooks.model

data class UseData (
    var userId: String?="",
    var name: String?="",
    var number: String?="",
    var imageUrl: String?="",
){
    fun toMap()= mapOf(
        "userID" to userId,
        "name" to name,
        "number" to number,
        "imageUrl" to imageUrl
    )
}
