package com.example.heybooks.view
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.MainViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditProfileScreen(viewModel: MainViewModel, actions: MainActions) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Chỉnh sửa thông tin cá nhân",
            color = Color.Black,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
//        val nameState = viewModel.nameState
//        val emailState = viewModel.emailState
        var nameState = "Nguyễn Luân"
        var emailState = "Luan123@gmail.com"

        OutlinedTextField(
//            value = nameState.value,
            value = nameState,

//            onValueChange = { nameState.value = it },
            onValueChange = { nameState = it },

            label = { Text("Tên người dùng") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Next) }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
//            value = emailState.value,
//            onValueChange = { emailState.value = it },
            value = emailState,

            onValueChange = { emailState = it },

            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
//                viewModel.updateProfile(nameState.value, emailState.value)
                actions.upPress()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Lưu")
        }
    }
}

