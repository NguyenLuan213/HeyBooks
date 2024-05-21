@file:Suppress("UNUSED_EXPRESSION")

package com.example.heybooks.view

import com.example.heybooks.components.AdminItemBookList
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.books.model.BookItems
import com.example.heybooks.R
import com.example.heybooks.components.DrawerContent
import com.example.heybooks.components.LoadingScreen
import com.example.heybooks.components.TextInputField
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.utils.ReviewState
import com.example.heybooks.utils.ViewState
import com.example.heybooks.viewmodel.MainViewModel

@SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun AdminBookListScreen(viewModel: MainViewModel = hiltViewModel(), actions: MainActions) {

    LaunchedEffect(Unit) {
        viewModel.getAllBooks()
    }

    Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = { androidx.compose.material.Text("Admin Dashboard") },
                backgroundColor = MaterialTheme.colors.primary,
                actions = {
                    androidx.compose.material.IconButton(onClick = { viewModel.logOut()
                        actions.gotoLogOut() }) {
                        androidx.compose.material.Icon(
                            Icons.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        drawerContent = {
            DrawerContent(viewModel, actions)
        },
        content = {
            ContentBookList(viewModel, actions)
        }
    )


}

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun ContentBookList(viewModel: MainViewModel = hiltViewModel(), actions: MainActions) {
    val viewState by viewModel.viewLiveData.observeAsState(initial = ViewState.Loading)
//    LaunchedEffect(Unit) {
//        viewModel.getAllBooks()
//    }
    when (val result = viewState) {
        ViewState.Loading -> LoadingScreen()
        is ViewState.Error -> Text(text = "Error found: ${result.exception.message}")
        is ViewState.Success -> {
            val search = remember { mutableStateOf("") }
            val listState = rememberLazyListState()
            val filteredBooks = result.data.filter { it.title.contains(search.value, ignoreCase = true) }
            var showEditDialog by remember { mutableStateOf(-1) }

            var showDelDialog by remember { mutableStateOf(-1) }
            var bookId by remember { mutableStateOf("") }
            var titleState by remember { mutableStateOf("") }
            var authorsState by remember { mutableStateOf("") }
            var categoriesState by remember { mutableStateOf("") }
            var bookintroductionState by remember { mutableStateOf("") }
            var bookcontentState by remember { mutableStateOf("") }
            var imageUrlState by remember { mutableStateOf("") }
            var uri by remember { mutableStateOf<Uri?>(null) }





//            val productPicker = rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.PickVisualMedia(),
//                onResult = { uri = it }
//            )
            var content = LocalContext.current

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .background(MaterialTheme.colors.background)
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
                        AdminItemBookList(
                            book = book,
                            onItemClick = {
                                actions.gotoAdminBookDetails(book.isbn)
                            },
                            onEditClick = {
                                titleState = book.title
                                authorsState = book.authors
                                categoriesState = book.categories
                                bookintroductionState = book.bookintroduction
                                bookcontentState = book.bookcontent
                                imageUrlState = book.imageUrl
                                content

                                // Hiển thị AlertDialog
                                showEditDialog = 1
                                bookId = book.isbn
                            },
                            onDeleteClick = {

                                showDelDialog = 1
                                bookId = book.isbn
                            }
                        )
                    }
                }
            }
            if (showDelDialog != -1) {
                DeleteBookScreen(viewModel = viewModel, actions = actions, isbn = bookId, showDelDialog = showDelDialog) {
                    showDelDialog = -1
                }
            }

            if (showEditDialog != -1) {
                UpdateBookScreen(
                    isbn = bookId,
                    showEditDialog = showEditDialog,
                    onDismiss = { showEditDialog = -1 },
                    titleState = titleState,
                    onTitleChange = { titleState = it },
                    authorsState = authorsState,
                    onAuthorsChange = { authorsState = it },
                    categoriesState = categoriesState,
                    onCategoriesChange = { categoriesState = it },
                    bookIntroductionState = bookintroductionState,
                    onBookIntroductionChange = { bookintroductionState = it },
                    bookContentState = bookcontentState,
                    onBookContentChange = { bookcontentState = it },
                    imageUrlState = imageUrlState,
                    onImageUrlChange = { imageUrlState = it },
                    viewModel
                )
            }
        }
        ViewState.Empty -> Text("No results found!")
    }
}

