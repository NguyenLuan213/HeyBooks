package com.example.heybooks.view

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.books.model.BookItems
import com.example.books.model.Review
import com.example.heybooks.components.CommentInput
import com.example.heybooks.components.CommentItem
import com.example.heybooks.components.LoadingScreen
import com.example.heybooks.model.UseData
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.utils.ReviewState
import com.example.heybooks.utils.ViewState
import com.example.heybooks.viewmodel.MainViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ReviewScreen(
    isbn: String, viewModel: MainViewModel, actions: MainActions
) {
    val reviewsState by viewModel.reviewsLiveData.observeAsState(initial = ReviewState.Loading)

    LaunchedEffect(Unit) {
        viewModel.loadReviews(isbn)
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
                val (content, input) = createRefs()

                LazyColumn(modifier = Modifier
                    .constrainAs(content) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(input.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(horizontal = 20.dp)) {
                    items(reviews) { reviewWithUserInfo ->
                        CommentItem(
                            nameUser = reviewWithUserInfo.userName,
                            thumbnailUrl = reviewWithUserInfo.imageUrl,
                            contentUser = reviewWithUserInfo.comment
                        )
                    }
                }

                Box(contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .constrainAs(input) {
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom, margin = 80.dp)
                        }
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    CommentInput(viewModel, actions, isbn)
                }
            }

        }
    }
}




