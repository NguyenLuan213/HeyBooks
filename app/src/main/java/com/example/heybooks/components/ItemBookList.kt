@file:OptIn(ExperimentalLayoutApi::class)

package com.example.heybooks.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter

import com.example.heybooks.ui.theme.primary
import com.example.heybooks.ui.theme.text
import com.example.heybooks.ui.theme.typography
import com.example.heybooks.utils.coloredShadow


val h2 = typography.displayLarge
val h3 = typography.displayMedium
val h4 = typography.displaySmall
val h5 = typography.headlineLarge
val h6 = typography.headlineSmall
val button = typography.labelLarge
val overline = typography.headlineMedium
val body1 = typography.bodyLarge
val body2 = typography.bodyMedium
val caption = typography.titleSmall
val subtitle2 = typography.titleMedium
val subtitle1 = typography.titleLarge
@Composable
fun ItemBookList(
    title: String,
    author: String,
    thumbnailUrl: String,
    categories: List<String>,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .clickable(onClick = onItemClick)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
            .clip(RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {

        // Row - Image + Content
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onSurface),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            Image(
                painter = rememberImagePainter(
                    data = thumbnailUrl
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(98.dp, 145.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Inside
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Content
            Column {
                Text(text = "b".plus(author), style = caption, color = text.copy(0.7F))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = title, style = subtitle1, color = text)
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow {
                    categories.forEach {
                        ChipView(category = it)
                    }
                }
            }

        }

    }
}



@Composable
fun ChipView(category: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(primary.copy(.10F))
            .padding(start = 12.dp, end = 12.dp, top = 5.dp, bottom = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = category, style = caption, color = primary)
    }
}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ItemBookListPreview() {
//    ItemBookList(book.title, book.authors.toString(), book.thumbnailUrl, book.categories)
//}