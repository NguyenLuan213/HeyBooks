package com.example.heybooks.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.heybooks.viewmodel.MainViewModel
import java.util.UUID

@Composable
fun UpdateBookScreen(
    isbn: String,
    showEditDialog: Int,
    onDismiss: () -> Unit,
    titleState: String,
    onTitleChange: (String) -> Unit,
    authorsState: String,
    onAuthorsChange: (String) -> Unit,
    categoriesState: String,
    onCategoriesChange: (String) -> Unit,
    bookIntroductionState: String,
    onBookIntroductionChange: (String) -> Unit,
    bookContentState: String,
    onBookContentChange: (String) -> Unit,
    imageUrlState: String,
    onImageUrlChange: (String) -> Unit,
    viewModel: MainViewModel
) {
    val context = LocalContext.current
    var imageUrl by remember { mutableStateOf(imageUrlState) }
    var uri by remember { mutableStateOf<Uri?>(null) }

    val productPicker =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { selectedUri ->
                selectedUri?.let {
                    uri = it
                    imageUrl = it.toString() // Update the imageUrl to the selected image's URI
                }
            })

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "Edit Book", style = TextStyle(color = Color(0xFF2196F3), fontSize = 20.sp)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(0.dp)
                    .wrapContentHeight(),

                ) {
                OutlinedTextField(
                    value = titleState,
                    onValueChange = onTitleChange,
                    label = { Text("Title") },
                    textStyle = TextStyle(Color.Black),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = authorsState,
                    onValueChange = onAuthorsChange,
                    label = { Text("Authors") },
                    textStyle = TextStyle(Color.Black),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = categoriesState,
                    onValueChange = onCategoriesChange,
                    label = { Text("Categories") },
                    textStyle = TextStyle(Color.Black),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = bookIntroductionState,
                    onValueChange = onBookIntroductionChange,
                    label = { Text("Book Introduction") },
                    textStyle = TextStyle(Color.Black),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = bookContentState,
                    onValueChange = onBookContentChange,
                    label = { Text("Book Content") },
                    textStyle = TextStyle(Color.Black),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (imageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(uri ?: imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp, 140.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onSurface)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                androidx.compose.material3.Button(
                    onClick = {
                        productPicker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3),
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.Gray
                    )
                ) {
                    Text("Choose Book Image")
                }
            }
        },
        confirmButton = {
            androidx.compose.material3.Button(
                onClick = {
                    uri?.let {
                        viewModel.uploadImg(
                            uri = it,
                            context = context,
                            imageName = UUID.randomUUID()
                                .toString()
                        ) { uploadedImageUrl ->
                            viewModel.updateBook(
                                isbn = isbn,
                                title = titleState,
                                authors = authorsState,
                                categories = categoriesState,
                                bookIntroduction = bookIntroductionState,
                                bookContent = bookContentState,
                                imageUrl = uploadedImageUrl
                            )
                            onImageUrlChange(uploadedImageUrl)
                            viewModel.deleteImg(imageUrlState)
                        }
                    } ?: run {
                        viewModel.updateBook(
                            isbn = isbn,
                            title = titleState,
                            authors = authorsState,
                            categories = categoriesState,
                            bookIntroduction = bookIntroductionState,
                            bookContent = bookContentState,
                            imageUrl = imageUrl
                        )
                    }

                    onDismiss()
                }, colors = ButtonDefaults.buttonColors(
                    Color(0xFF2196F3), contentColor = Color.White
                ), shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Edit")
            }
        },
        dismissButton = {
            androidx.compose.material3.Button(
                onClick = { onDismiss() }, colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                ), shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Cancel")
            }
        },
        modifier = Modifier
            .padding(vertical = 10.dp)
            .background(color = Color(0xFFe5ecfe)),
        backgroundColor = Color(0xFFe5ecfe)

    )
}
