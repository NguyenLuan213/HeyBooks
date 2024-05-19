package com.example.heybooks.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.books.model.BookItems
import com.example.books.model.Review
import com.example.heybooks.model.UseData
import com.example.heybooks.viewmodel.MainViewModel

@Composable
fun ReviewScreen(
    viewModel: MainViewModel,
    book: BookItems
) {
}

@Composable
fun CommentCard(review: Review, user: UseData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = rememberImagePainter(data = user.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = user.name ?: "Unknown User", style = MaterialTheme.typography.subtitle1)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = review.comment, style = MaterialTheme.typography.body1)
            }
        }
    }
}


