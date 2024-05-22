package com.example.heybooks.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.books.model.BookItems
import com.example.heybooks.R
import com.example.heybooks.components.BookDetailsCard
import com.example.heybooks.components.LoadingScreen
import com.example.heybooks.components.TopBar
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.ui.theme.typography
import com.example.heybooks.utils.DetailViewState
import com.example.heybooks.viewmodel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun BookDetailsScreen(viewModel: MainViewModel, actions: MainActions, isbn: String?) {
    if (!isbn.isNullOrEmpty()) {
        LaunchedEffect(isbn) {
            viewModel.getBookByID(isbn)
        }
    }
    val detailViewState by viewModel.bookDetails.collectAsState()

    Scaffold(topBar = {
        TopBar(title = stringResource(id = R.string.text_bookDetails), action = actions)
    }) {

        BookDetails(viewModel = viewModel, actions)
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BookDetails(viewModel: MainViewModel, actions: MainActions) {
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
                BookDetailsTab(book = result.data, actions, viewModel)
            }

        }

        DetailViewState.Empty -> Text("No results found!")
    }
}


@Composable
fun BookDetailsTab(book: BookItems, actions: MainActions, viewModel: MainViewModel) {
    val tabTitles = listOf("Introduce", "Comment")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column() {
        TabRow(selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.Gray, // Màu nền của TabRow
            contentColor = Color(0xFF558BEB),  // Màu của indicator
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .height(3.dp) // Chiều cao của indicator
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

                    selectedContentColor = Color(0xFF749EE9),  // Màu khi tab được chọn
                    unselectedContentColor = Color.LightGray
                )
            }
        }
        when (selectedTabIndex) {
            0 -> TabContent1(book, actions)
            1 -> TabContent2(book, actions, viewModel)
        }
    }
}

@Composable
fun TabContent1(book: BookItems, actions: MainActions) {
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
                .padding(bottom = 50.dp)
        ) {
            Button(
                onClick = { actions.gotoBookReading(bookContent) },
                modifier = Modifier
                    .height(70.dp)
                    .width(300.dp)
                    .padding(bottom = 16.dp)
                    .alpha(0.7f)
                    .background(Color.Transparent),
                shape = RoundedCornerShape(48.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF7eafc8))

            ) {
                Text(text = "Book Content")
            }
        }
    }


}

@Composable
fun TabContent2(book: BookItems, actions: MainActions, viewModel: MainViewModel) {
    ReviewScreen(book.isbn,viewModel,actions)
}





