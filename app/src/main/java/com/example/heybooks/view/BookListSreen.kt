package com.example.heybooks.view

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.books.model.BookItems
import com.example.heybooks.R
import com.example.heybooks.components.ItemBookList
import com.example.heybooks.components.LoadingScreen
import com.example.heybooks.components.TextInputField
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.utils.ViewState
import com.example.heybooks.viewmodel.MainViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@ExperimentalComposeUiApi
@Composable
fun BookListScreen(viewModel: MainViewModel = hiltViewModel(), actions: MainActions) {
    val viewState by viewModel.viewLiveData.observeAsState(initial = ViewState.Loading)
    when (val result = viewState) {
        ViewState.Loading -> LoadingScreen()
        is ViewState.Error -> Text(text = "Error found: ${result.exception.message}")
        is ViewState.Success -> BookList(result.data, viewModel, actions)
        ViewState.Empty -> Text("No results found!")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalComposeUiApi
@Composable
fun BookList(bookList: List<BookItems>, viewModel: MainViewModel, actions: MainActions) {
//    viewModel.getAllBooks()
    val search = remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val filteredBooks = bookList.filter { it.title.contains(search.value, ignoreCase = true) }

    LazyColumn(
        state = listState,
        modifier = Modifier.background(Color(0xfff3f4f8)).padding(bottom = 55.dp)
    ) {
        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.onPrimary)
                    .padding(10.dp)
            ) {
                TextInputField(
                    label = stringResource(R.string.text_search),
                    value = search.value,
                    onValueChanged = { search.value = it }
                )
            }
        }

        if (filteredBooks.isEmpty()) {
            item {
                Text(
                    text = "No result",
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        } else {
            items(filteredBooks) { book ->
                ItemBookList(
                    book = book,
                    isBookmarked = false, // Set initial state based on your logic
                    onItemClick = {
                        actions.gotoBookDetails.invoke(book.isbn)
                    },
                    onSaveClick = {
                        viewModel.saveBookForUser(book)
                    },
                    onRemoveClick = {
                        viewModel.removeBookForUser(book)
                    }
                )
            }
        }
    }
}
