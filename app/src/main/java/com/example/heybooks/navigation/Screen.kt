package com.example.heybooks.navigation

import androidx.annotation.StringRes
import com.example.heybooks.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Login : Screen("Login", R.string.text_bookList)
    object SignUp : Screen("SignUp", R.string.text_bookList)
    object LogOut : Screen("LogOut", R.string.text_bookList)
    object Saved : Screen("Saved", R.string.text_bookList)
    object Profile : Screen("Profile", R.string.text_bookList)
    object EditProfile : Screen("EditProfile", R.string.text_bookList)

    object BookList : Screen("book_list", R.string.text_bookList)
    object Details : Screen("book_details", R.string.text_bookDetails)
}