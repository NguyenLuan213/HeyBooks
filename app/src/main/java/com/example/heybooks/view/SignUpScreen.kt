package com.example.heybooks.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heybooks.R
import com.example.heybooks.components.ClickTextLogin
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.CheckSignedIn
import com.example.heybooks.viewmodel.MainViewModel

@Composable
fun SignUpScreen(viewModel: MainViewModel, actions: MainActions) {
//    val backgroundImage: Painter = painterResource(id = R.drawable.background_loginsignup)
    CheckSignedIn(viewModel = viewModel, actions)
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val backgroundImage: Painter = painterResource(id = R.drawable.background_loginsignup)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = backgroundImage,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,

            )

        val nameState = remember {
            mutableStateOf(TextFieldValue())
        }
        val numberState = remember {
            mutableStateOf(TextFieldValue())
        }
        val emailState = remember {
            mutableStateOf(TextFieldValue())
        }
        val passwordState = remember {
            mutableStateOf(TextFieldValue())
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 80.dp, bottom = 0.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Chào bạn,",
                modifier = Modifier
                    .align(Alignment.Start),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                lineHeight = 18.sp,
                color = Color.LightGray
            )
            Text(
                text = "Đăng nhập để tiếp tục",
                modifier = Modifier
                    .align(Alignment.Start),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.height(100.dp))
            OutlinedTextField(
                value = nameState.value,
                onValueChange = { nameState.value = it },
                label = { Text(text = "Tên tài khoản") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Next) }
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = numberState.value,
                onValueChange = { numberState.value = it },
                label = { Text(text = "Số đt") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Next) }
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text(text = "Email") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(focusDirection = FocusDirection.Next) }
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text(text = "Mật khẩu") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                ),
                shape = RoundedCornerShape(16.dp),
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    viewModel.signUp(
                        name = nameState.value.text,
                        number = numberState.value.text,
                        email = emailState.value.text,
                        password = passwordState.value.text,
                        context = context
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Đăng Ký", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))
            ClickTextLogin(actions)

        }
    }
}


//if(vm.inProcess.value){
//    CommonProgressBar()
//}



