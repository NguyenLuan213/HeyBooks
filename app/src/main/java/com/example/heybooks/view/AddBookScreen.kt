import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.heybooks.components.DrawerContent
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.MainViewModel


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddBookScreen(
    viewModel: MainViewModel,
    actions: MainActions
) {
    androidx.compose.material.Scaffold(
        topBar = {
            androidx.compose.material.TopAppBar(
                title = { androidx.compose.material.Text("Admin Dashboard") },
                backgroundColor = MaterialTheme.colors.primary,
                actions = {
                    androidx.compose.material.IconButton(onClick = {viewModel.logOut()
                        actions.gotoLogOut() }) {
                        androidx.compose.material.Icon(
                            Icons.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        drawerContent = {
            DrawerContent(viewModel, actions)
        },
        content = {
            AddBook(viewModel, actions)
        }
    )


}

@Composable
fun AddBook(
    viewModel: MainViewModel,
    actions: MainActions
) {

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    var isbnState by remember { mutableStateOf(TextFieldValue()) }
    var titleState by remember { mutableStateOf(TextFieldValue()) }
    var authorsState by remember { mutableStateOf(TextFieldValue()) }
    var categoriesState by remember { mutableStateOf(TextFieldValue()) }
    var bookcontentState by remember { mutableStateOf(TextFieldValue()) }
    var bookintroductionState by remember { mutableStateOf(TextFieldValue()) }
    var imageLoaded by remember { mutableStateOf(false) }
    var isAllFieldsFilled by remember { mutableStateOf(false) }
    isAllFieldsFilled = (titleState.text.isNotBlank() && authorsState.text.isNotBlank() && categoriesState.text.isNotBlank() && bookintroductionState.text.isNotBlank() && bookcontentState.text.isNotBlank())

    var uri by remember { mutableStateOf<Uri?>(null) }

    val productPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri = it }
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Add New Book",
                style = MaterialTheme.typography.h4,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = titleState,
                onValueChange = { titleState = it },
                label = { Text("Title") },
                textStyle = TextStyle(Color.Black),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                })

            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = authorsState,
                onValueChange = { authorsState = it },
                label = { Text("Authors") },
                textStyle = TextStyle(Color.Black),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = categoriesState,
                onValueChange = { categoriesState = it },
                label = { Text("Categories") },
                textStyle = TextStyle(Color.Black),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = bookintroductionState,
                onValueChange = { bookintroductionState = it },
                label = { Text("Book Introduction") },
                textStyle = TextStyle(Color.Black),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                })
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = bookcontentState,
                onValueChange = { bookcontentState = it },
                label = { Text("Book Content") },
                textStyle = TextStyle(Color.Black),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uri != null) {
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp, 140.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(androidx.compose.material3.MaterialTheme.colorScheme.onSurface)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
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

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    uri?.let {
                        if (isAllFieldsFilled) { // Only proceed if all fields are filled
                            viewModel.uploadToStorage(
                                uri = it,
                                context = context,
                                type = "image"
                            ) { imageUrl ->
                                viewModel.addBook(
                                    titleState.text,
                                    authorsState.text,
                                    categoriesState.text,
                                    bookintroductionState.text,
                                    bookcontentState.text,
                                    imageUrl,
                                    context
                                )
                                isbnState = TextFieldValue()
                                titleState = TextFieldValue()
                                authorsState = TextFieldValue()
                                categoriesState = TextFieldValue()
                                bookintroductionState = TextFieldValue()
                                bookcontentState = TextFieldValue()
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                uri = null
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF5722),
                    contentColor = Color.White,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.Gray
                ),
                enabled = isAllFieldsFilled
            ) {
                Text("Add Book")
            }
        }
    }
}
