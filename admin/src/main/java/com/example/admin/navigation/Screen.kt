package com.example.heybooks.navigation

import androidx.annotation.StringRes
import com.example.admin.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object HomeScreen : Screen("HomeScreen", R.string.app_name)
    object Add : Screen("Add",R.string.app_name)
    object Display : Screen("Display",R.string.app_name)
}