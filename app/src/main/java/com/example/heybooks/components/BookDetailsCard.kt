package com.example.heybooks.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.heybooks.R
import com.example.heybooks.ui.theme.text

@Composable
fun BookDetailsCard(
    title: String,
    authors: String,
    thumbnailUrl: String,
    categories: String
) {

    // Transparent white bg
    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 20.dp, end = 16.dp, top = 40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color.Transparent),
        contentAlignment = Alignment.Center
    ) {

        // white box layout
        Box(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.onSurface),
        )

        // Content
        BookImageContentView(title, authors, thumbnailUrl, categories)


    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookImageContentView(
    title: String,
    authors: String,
    thumbnailUrl: String,
    categories: String
) {
    // content
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

        // image
        Image(
            painter = rememberImagePainter(
                data = thumbnailUrl
            ),
            contentDescription = title,
            modifier = Modifier
                .size(240.dp, 140.dp),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onSurface)
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = text
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = authors.toString(),
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                color = text.copy(0.7F)
            )
//            Spacer(modifier = Modifier.height(12.dp))
//            FlowRow {
//                categories.forEach {
//                    ChipView(category = it)
//                }
//            }
        }
    }
}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun Greetings() {
//    BookDetailsCard()
//}