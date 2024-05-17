package com.example.heybooks.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heybooks.R
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.MainViewModel

@Composable
fun ProfileScreen(viewModel: MainViewModel, actions: MainActions) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_image), // Thay thế bằng hình ảnh của người dùng
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(120.dp)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Tên người dùng", fontSize = 24.sp) // Replace with the user's name

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Email: example@example.com") // Thay thế bằng email của người dùng

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { actions.gotoEditProfile() },
            colors = ButtonDefaults.buttonColors(Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                Icons.Filled.Settings,
                contentDescription = "Edit Profile"
            ) // Icon chỉnh sửa thông tin cá nhân
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Chỉnh sửa thông tin cá nhân")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.logOut()
                      actions.gotoLogOut() },
            colors = ButtonDefaults.buttonColors(Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Đăng xuất")
        }
    }
}
