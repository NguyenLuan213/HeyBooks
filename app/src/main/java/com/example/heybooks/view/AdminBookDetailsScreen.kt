package com.example.heybooks.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.books.model.BookItems
import com.example.heybooks.R
import com.example.heybooks.components.AdminCommentItem
import com.example.heybooks.components.BookDetailsCard
import com.example.heybooks.components.CommentInput
import com.example.heybooks.components.CommentItem
import com.example.heybooks.components.LoadingScreen
import com.example.heybooks.components.TopBar
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.ui.theme.typography
import com.example.heybooks.utils.DetailViewState
import com.example.heybooks.utils.ReviewState
import com.example.heybooks.viewmodel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AdminBookDetailsScreen(viewModel: MainViewModel, actions: MainActions, isbn: String?) {
    if (!isbn.isNullOrEmpty()) {
        LaunchedEffect(isbn) {
            viewModel.getBookByID(isbn)
        }
    }
    val detailViewState by viewModel.bookDetails.collectAsState()

    Scaffold(topBar = {
        TopBar(title = stringResource(id = R.string.text_bookDetails), action = actions)
    }) {

        AdminBookDetails(viewModel = viewModel, actions)
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AdminBookDetails(viewModel: MainViewModel, actions: MainActions) {
    when (val result = viewModel.bookDetails.value) {
        DetailViewState.Loading -> LoadingScreen()
        is DetailViewState.Error -> Text(text = "Error found: ${result.exception}")
        is DetailViewState.Success -> {
            val book = result.data

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BookDetailsCard(book.title, book.authors, book.imageUrl, book.categories)
                AdminBookDetailsTab(book = result.data, actions, viewModel)
            }

        }

        DetailViewState.Empty -> Text("No results found!")
    }
}


@Composable
fun AdminBookDetailsTab(book: BookItems, actions: MainActions, viewModel: MainViewModel) {
    val tabTitles = listOf("Introduce", "Comment")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column() {
        TabRow(selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.Gray, // Màu nền của TabRow
            contentColor = Color.Red,  // Màu của indicator
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(4.dp) // Chiều cao của indicator
                        .background(Color.Yellow) // Màu của indicator
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            color = if (selectedTabIndex == index) Color.Black else Color.Gray
                        )
                    },
                    modifier = Modifier.background(Color.White),

                    selectedContentColor = Color.Yellow,  // Màu khi tab được chọn
                    unselectedContentColor = Color.LightGray
                )
            }
        }
        when (selectedTabIndex) {
            0 -> AdminTabContent1(book, actions)
            1 -> AdminTabContent2(book, actions, viewModel)
        }
    }
}

@Composable
fun AdminTabContent1(book: BookItems, actions: MainActions) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (content, button) = createRefs()
        LazyColumn(
            modifier = Modifier
                .constrainAs(content) {
                    top.linkTo(parent.top)
                    bottom.linkTo(button.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(horizontal = 20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(id = R.string.text_bookDetails),
                    style = typography.titleLarge,
                    color = MaterialTheme.colors.primaryVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = book.bookintroduction,
                    style = typography.bodyLarge,
                    textAlign = TextAlign.Justify,
                    color = MaterialTheme.colors.primaryVariant.copy(0.7F)
                )
            }
        }
        val bookContent = book.bookcontent
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.1f))
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom)
                }
                .padding(bottom = 10.dp)
        ) {
            Button(
                onClick = { actions.gotoAdminBookReading(bookContent) },
                modifier = Modifier
                    .height(70.dp)
                    .width(300.dp)
                    .padding(bottom = 16.dp)
                    .alpha(0.7f)
                    .background(Color.Transparent),
                shape = RoundedCornerShape(48.dp)

            ) {
                Text(text = "Book Content")
            }
        }
    }


}

@Composable
fun AdminTabContent2(book: BookItems, actions: MainActions, viewModel: MainViewModel) {
    val content = LocalContext.current
    val reviewsState by viewModel.reviewsLiveData.observeAsState(initial = ReviewState.Loading)

    LaunchedEffect(Unit) {
        viewModel.loadReviews(book.isbn)
    }
    when (val result = reviewsState) {
        ReviewState.Loading -> LoadingScreen()
        is ReviewState.Empty -> Text(text = "There are no reviews yet")
        is ReviewState.Error -> Text(text = "Error found: ${result.exception.message}")
        is ReviewState.Success -> {

            val reviews = result.data

            ConstraintLayout(
                modifier = Modifier.fillMaxSize()
            ) {
                val (contents, input, deleteIcon) = createRefs()

                LazyColumn(modifier = Modifier
                    .constrainAs(contents) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(input.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 20.dp)) {
                    items(reviews) { reviewWithUserInfo ->
                        AdminCommentItem(
                            reviewId = reviewWithUserInfo.reviewId,
                            isbn = reviewWithUserInfo.isbn,
                            nameUser = reviewWithUserInfo.userName,
                            thumbnailUrl = reviewWithUserInfo.imageUrl,
                            contentUser = reviewWithUserInfo.comment,
                            viewModel
                        )
                    }
                }

                Box(contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .constrainAs(input) {
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom, margin = 10.dp)
                        }
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    CommentInput(viewModel, actions, book.isbn)
                }




            }

        }
    }
}





