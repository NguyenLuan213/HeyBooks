package com.example.heybooks.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.ui.theme.text
import com.example.heybooks.viewmodel.MainViewModel

@Composable
fun AddBookScreen(
    viewModel: MainViewModel,
    actions: MainActions
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val isbnState = remember { mutableStateOf(TextFieldValue()) }
    val titleState = remember { mutableStateOf(TextFieldValue()) }
    val authorsState = remember { mutableStateOf(TextFieldValue()) }
    val categoriesState = remember { mutableStateOf(TextFieldValue()) }
    val bookcontentState = remember { mutableStateOf(TextFieldValue()) }
    val bookintroductionState = remember { mutableStateOf(TextFieldValue()) }


    var uri by remember {
        mutableStateOf<Uri?>(null)
    }
    val productPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            uri = it
        }
    )
    val State = remember {
        mutableStateOf(TextFieldValue())
    }

    Box(
        modifier = Modifier.fillMaxSize()

    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {


            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = titleState.value,
                onValueChange = { titleState.value = it },
                label = { Text("Title") },
                textStyle = TextStyle(Color.Black),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = authorsState.value,
                onValueChange = { authorsState.value = it },
                label = { Text("Authors") },
                textStyle = TextStyle(Color.Black),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = categoriesState.value,
                onValueChange = { categoriesState.value = it },
                label = { Text("Categories") },
                textStyle = TextStyle(Color.Black),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = bookintroductionState.value,
                onValueChange = { bookintroductionState.value = it },
                label = { Text("Book content") },
                textStyle = TextStyle(Color.Black),
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = bookcontentState.value,
                onValueChange = { bookcontentState.value = it },
                label = { Text("Book content") },
                textStyle = TextStyle(Color.Black),
                modifier = Modifier.fillMaxWidth(),
            )


            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(32.dp))
            AsyncImage(model = uri, contentDescription = null, modifier = Modifier.size(200.dp))

            Button(
                onClick = {
                    productPicker.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }, modifier = Modifier.size(280.dp, 50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF2196f3)
                )
            ) {
                Text("Chọn ảnh sản phẩm ")
            }


            Button(
                onClick = {
                    uri?.let {
                        viewModel.uploadToStorage(
                            uri = it,
                            context = context,
                            type = "image"
                        ) { imageUrl ->
                            // Sau khi tải ảnh lên và nhận được đường dẫn, gọi hàm addData với đường dẫn này
                            viewModel.addBook(
                                titleState.value.text,
                                authorsState.value.text,
                                categoriesState.value.text,
                                bookintroductionState.value.text,
                                bookcontentState.value.text,
                                imageUrl,
                                context
                            )
// Clear the input fields
                            isbnState.value = TextFieldValue()
                            titleState.value = TextFieldValue()
                            authorsState.value = TextFieldValue()
                            categoriesState.value = TextFieldValue()
                            bookintroductionState.value = TextFieldValue()
                            bookcontentState.value = TextFieldValue()
// Close the keyboard
                            keyboardController?.hide()
// Focus the first input field
                            focusManager.clearFocus()
                        }
                    }

                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Add Book")
            }
        }
    }
}



