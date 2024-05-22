package com.example.heybooks.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.example.heybooks.R
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.ui.theme.text
import com.example.heybooks.viewmodel.MainViewModel


@Composable
fun CommentInput(viewModel: MainViewModel, actions: MainActions, isbn : String?) {
    val context = LocalContext.current
    var commentText = remember { mutableStateOf(TextFieldValue()) }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = commentText.value,
            onValueChange = { commentText.value = it },
            modifier = Modifier
                .weight(1f)
                .background(
                    color = Color.White
                ),
            shape = RoundedCornerShape(24.dp),
            placeholder = { Text(text = "Add a comment...") },
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black, // Màu con trỏ
                focusedIndicatorColor = Color.Transparent, // Loại bỏ viền dưới khi focused
                unfocusedIndicatorColor = Color.Transparent, // Loại bỏ viền dưới khi unfocused
                disabledIndicatorColor = Color.Transparent // Loại bỏ viền dưới khi disabled
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = {
                if (commentText.value.text.isNotEmpty()) {
                    if (isbn != null) {
                        viewModel.addReview(isbn ,commentText.value.text, context)
                    }
                }
                commentText.value = TextFieldValue()
                keyboardController?.hide()
                focusManager.clearFocus()

            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0583F2)), // Màu nền của nút
            shape = RoundedCornerShape(48.dp),
            modifier = Modifier.align(Alignment.CenterVertically)
        ) {
            Text(text = "Post", color = Color.White) // Màu chữ của nút
        }
    }
}


@Composable
fun CommentItem(
    nameUser: String,
    thumbnailUrl: String,
    contentUser: String
) {
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

            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                ConstraintLayout {
                    val (imgAvt, txtname, txtcontent) = createRefs()
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .constrainAs(imgAvt) {
                                top.linkTo(parent.top, margin = 10.dp)
                                start.linkTo(parent.start, margin = 10.dp)
                            }

                    ) {
                        if (thumbnailUrl != null){
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
                        }else{
                            Image(
                                painter = painterResource(id = R.drawable.avatar),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop,
                            )
                        }

                    }
                    Box(modifier = Modifier.constrainAs(txtname) {
                        top.linkTo(parent.top, margin = 20.dp)
                        start.linkTo(imgAvt.end, margin = -50.dp)

                    }) {
                        Text(text = nameUser, style = subtitle1, color = text.copy(0.7F))
                    }
                    Box(modifier = Modifier
                        .constrainAs(txtcontent) {
                            top.linkTo(imgAvt.bottom, margin = -70.dp)
                            start.linkTo(imgAvt.end, margin = -110.dp)

                        }
                        .padding(horizontal = 20.dp, vertical = 20.dp)) {
                        Text(text = contentUser, style = caption, color = text.copy(0.7F))
                    }
                }
            }


//            Spacer(modifier = Modifier.width(16.dp))
//
//            Column {
//                Text(text = "", style = caption, color = text.copy(0.7F))
//                Spacer(modifier = Modifier.height(8.dp))
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//
//
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//            }
        }
    }
}
