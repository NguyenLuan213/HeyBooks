package com.example.heybooks.Data

open class Event <out T>(val content: T) {
    var hasBeenhandle = false
    fun getContentOrNull():T? {
        return if(hasBeenhandle) null
        else{
            hasBeenhandle = true
            content
        }
    }
}