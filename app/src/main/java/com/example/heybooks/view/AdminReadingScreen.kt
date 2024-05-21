package com.example.heybooks.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.heybooks.R
import com.example.heybooks.components.TopBar
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.MainViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AdminReadingScreen(
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            if (!bookContent.isNullOrEmpty()) {
                Text(
                    text = bookContent,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(text = "No content available")
            }
        }

    }
}
