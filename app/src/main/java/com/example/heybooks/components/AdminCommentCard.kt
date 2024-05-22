package com.example.heybooks.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter
import com.example.heybooks.R
import com.example.heybooks.ui.theme.text
import com.example.heybooks.viewmodel.MainViewModel

@Composable
fun AdminCommentItem(
    reviewId : String,
    isbn : String,
    nameUser: String,
    thumbnailUrl: String,
    contentUser: String,
    viewModel: MainViewModel
) {
    val content = LocalContext.current
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
            .clip(RoundedCornerShape(20.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFeaeffd)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                ConstraintLayout {
                    val (imgAvt, txtname, txtcontent) = createRefs()
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .constrainAs(imgAvt) {
                                top.linkTo(parent.top, margin = 10.dp)
                                start.linkTo(parent.start, margin = 10.dp)
                            }
                    )
                    {
                        Image(
                            painter = rememberImagePainter(
                                data = thumbnailUrl
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                        )
                    }

                    Box(
                        modifier = Modifier.constrainAs(txtname) {
                            top.linkTo(parent.top, margin = 20.dp)
                            start.linkTo(imgAvt.end, margin = -50.dp)
                        }
                    ) {
                        Text(text = nameUser, style = subtitle1, color = text.copy(0.7F))
                    }
                    Box(
                        modifier = Modifier
                            .constrainAs(txtcontent) {
                                top.linkTo(imgAvt.bottom, margin = -70.dp)
                                start.linkTo(imgAvt.end, margin = -110.dp)
                            }
                            .padding(horizontal = 20.dp, vertical = 20.dp)
                    ) {
                        Text(text = contentUser, style = caption, color = text.copy(0.7F))
                    }

                }
            }
            Column {
                IconButton(
                    onClick = { viewModel.deleteReviews(reviewId = reviewId, isbn = isbn, context = content)},
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

