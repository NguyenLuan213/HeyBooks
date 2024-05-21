package com.example.heybooks.navigation

import androidx.annotation.StringRes
import com.example.heybooks.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Reading : Screen("Reading", R.string.text_bookList)
    object AdminReading : Screen("AdminReading", R.string.text_bookList)
    object AddBook : Screen("AddBook", R.string.text_bookList)

    object UpdateBook : Screen("UpdateBook", R.string.text_bookList)
    object AdminBookList : Screen("AdminBookList", R.string.text_bookList)
    object DeleteBook : Screen("DeleteBook", R.string.text_bookList)
    object AdminDetails : Screen("AdminDetails", R.string.text_bookDetails)

    object Login : Screen("Login", R.string.text_bookList)

    object SignUp : Screen("SignUp", R.string.text_bookList)
    object LogOut : Screen("LogOut", R.string.text_bookList)
    object Saved : Screen("Saved", R.string.text_bookList)
    object Profile : Screen("Profile", R.string.text_bookList)
    object EditProfile : Screen("EditProfile", R.string.text_bookList)

    object BookList : Screen("book_list", R.string.text_bookList)
    object Details : Screen("book_details", R.string.text_bookDetails)
}