package com.example.heybooks.view

import android.annotation.SuppressLint
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.heybooks.R
import com.example.heybooks.components.TopBar
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.MainViewModel

//@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
//@Composable
//fun ReadingScreen(
//    viewModel: MainViewModel, actions: MainActions, bookcontext: String?
//) {
//
//    Scaffold(topBar = {
//        TopBar(title = stringResource(id = R.string.text_bookDetails), action = actions)
//    }) {
////        ReadABook(bookTitle = "book.title", bookContext?)
//        if (bookcontext != null) {
//            BasicText(text = bookcontext)
//        }
//    }
//}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReadingScreen(
    viewModel: MainViewModel, actions: MainActions, bookContent: String?
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(id = R.string.text_bookDetails),
                action = actions
            )
        }
    ) {
        if (!bookContent.isNullOrEmpty()) {
            Text(text = bookContent)
        } else {
            Text(text = "No content available")
        }
    }
}
