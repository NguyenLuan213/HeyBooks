package com.example.heybooks.view

import android.annotation.SuppressLint
import com.example.heybooks.viewmodel.MainViewModel


import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.books.model.BookItem
import com.example.heybooks.R
import com.example.heybooks.components.ItemBookList
import com.example.heybooks.components.OptionMenu
import com.example.heybooks.components.TextInputField
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.utils.ViewState
import com.example.heybooks.utils.coloredShadow


@SuppressLint("StateFlowValueCalledInComposition")
@ExperimentalComposeUiApi
@Composable
fun BookListScreen(viewModel: MainViewModel, actions: MainActions) {

    when (val result = viewModel.books.value) {
        ViewState.Loading -> Text(text = "Loading")
        is ViewState.Error -> Text(text = "Error found: ${result.exception}")
        is ViewState.Success -> {
            BookList(result.data, actions)
        }

        ViewState.Empty -> Text("No results found!")
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalComposeUiApi
@Composable
fun BookList(bookList: List<BookItem>, actions: MainActions) {

    val search = remember {
        mutableStateOf("")
    }

    val listState = rememberLazyListState()

    val filteredBooks = bookList.filter { it.title.contains(search.value, ignoreCase = true) }

    LazyColumn(state = listState, modifier = Modifier.background(MaterialTheme.colors.background)) {

        stickyHeader {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
                    .padding(10.dp)
            )
            {
                TextInputField(
                    label = stringResource(R.string.text_search),
                    value = search.value,
                    onValueChanged = {
                        search.value = it
                    }
                )
            }
        }

        item {
            Text(
                text = "Famous books",
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 16.dp, end = 24.dp, top = 8.dp)
            )
        }

        if (filteredBooks.isEmpty()) {
            item {
                Text(
                    text = "No result",
                    style = MaterialTheme.typography.subtitle1,
//                    color = MaterialTheme.colors.onPrimary,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        } else {
            items(filteredBooks) { book ->
                Log.d("books", "books are ${book.title}")

                ItemBookList(
                    title = book.title,
                    author = book.authors.toString(),
                    thumbnailUrl = book.thumbnailUrl,
                    categories = book.categories,
                    onItemClick = {
                        actions.gotoBookDetails.invoke(book.isbn)
                    }
                )
            }
        }
    }
}

