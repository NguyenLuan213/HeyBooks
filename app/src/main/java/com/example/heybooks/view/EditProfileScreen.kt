package com.example.heybooks.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.heybooks.R
import com.example.heybooks.components.LoadingScreen
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.utils.UsersViewState
import com.example.heybooks.viewmodel.MainViewModel
import java.util.UUID

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(viewModel: MainViewModel, actions: MainActions) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val usersViewState by viewModel.usersViewState.observeAsState(initial = UsersViewState.Loading)

    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    when (val result = usersViewState) {
        UsersViewState.Loading -> LoadingScreen()
        is UsersViewState.Error -> androidx.compose.material.Text(text = "Error found: ${result.exception}")
        is UsersViewState.Success -> {
            val userData = result.data.firstOrNull()
            var userIdState by remember { mutableStateOf(userData?.userId ?: "") }
            var nameState by remember { mutableStateOf(userData?.name ?: "") }
            var phoneState by remember { mutableStateOf(userData?.number ?: "") }
            var imageUrlState = userData?.imageUrl ?: ""

            var imageUrl by remember { mutableStateOf(imageUrlState) }
            var uri by remember { mutableStateOf<Uri?>(null) }

            val productPicker =
                rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia(),
                    onResult = { selectedUri ->
                        selectedUri?.let {
                            uri = it
                            imageUrl =
                                it.toString() // Update the imageUrl to the selected image's URI
                        }
                    })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Edit personal information", color = Color.Black, fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (imageUrl.isNotEmpty()) {
                    Image(
                        painter = rememberAsyncImagePainter(uri ?: imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp, 150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onSurface)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.avatar), // Thay thế bằng hình ảnh của người dùng
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp, 150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onSurface)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                }

                OutlinedTextField(
                    value = nameState,
                    onValueChange = { nameState = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    })
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = phoneState,
                    onValueChange = { phoneState = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        color = Color.Black,
                        fontSize = 16.sp,
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email, imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween // Cách đều 2 nút ra hai bên
                ) {
                    Button(
                        onClick = {
                            productPicker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            contentColor = Color.Black.copy(0.8f),
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.Gray
                        )
                    ) {
                        androidx.compose.material.Text("Choose Image")
                    }
                    Button(
                        onClick = {
                            uri?.let {
                                viewModel.uploadImg(
                                    uri = it,
                                    context = context,
                                    imageName = UUID.randomUUID().toString() // Generate a unique name for the new image
                                ) { uploadedImageUrl ->
                                    viewModel.updateProfile(
                                        uid = userIdState,
                                        name = nameState,
                                        number = phoneState,
                                        imageUrl = uploadedImageUrl,
                                        context = context
                                    )
                                    viewModel.deleteImg(imageUrlState) // Delete the old image
                                }
                            } ?: run {
                                viewModel.updateProfile(
                                    uid = userIdState,
                                    name = nameState,
                                    number = phoneState,
                                    imageUrl = imageUrlState,
                                    context = context
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            contentColor = Color.Black.copy(0.8f),
                            disabledContainerColor = Color.LightGray,
                            disabledContentColor = Color.Gray
                        )
                    ) {
                        Text("Save")
                    }
                }
            }
        }

        UsersViewState.Empty -> Text(text = "Empty")
    }
}


