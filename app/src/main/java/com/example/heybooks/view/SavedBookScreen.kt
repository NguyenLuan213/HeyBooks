package com.example.heybooks.view

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.MainViewModel

@Composable
fun SavedBookScreen(viewModel: MainViewModel, actions: MainActions) {
    Text(text = "Đây là màn hình SeverBook")

}