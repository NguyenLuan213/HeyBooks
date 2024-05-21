package com.example.heybooks.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.heybooks.R
import com.example.heybooks.components.LoadingScreen
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.utils.DetailViewState
import com.example.heybooks.utils.UsersViewState
import com.example.heybooks.utils.ViewState
import com.example.heybooks.viewmodel.MainViewModel
import com.google.firebase.firestore.auth.User

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ProfileScreen(viewModel: MainViewModel, actions: MainActions) {
    val usersViewState by viewModel.usersViewState.observeAsState(initial = UsersViewState.Loading)
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.loadUser()
    }

    when (val result = usersViewState) {
        UsersViewState.Loading -> LoadingScreen()
        is UsersViewState.Error -> androidx.compose.material.Text(text = "Error found: ${result.exception}")
        is UsersViewState.Success -> {
            val userData = result.data.firstOrNull()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (userData?.imageUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(model = userData.imageUrl), // Thay thế bằng hình ảnh của người dùng
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp, 150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onSurface)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                }else{
                    Image(
                        painter = painterResource(id = R.drawable.avatar), // Thay thế bằng hình ảnh của người dùng
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(120.dp, 150.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.onSurface)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Crop
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                userData?.name?.let { Text(text = it, fontSize = 24.sp) }

                Spacer(modifier = Modifier.height(16.dp))

                userData?.number?.let { Text(text = it) }

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
                    Text(text ="Edit personal information")
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.logOut()
                        actions.gotoLogOut() },
                    colors = ButtonDefaults.buttonColors(Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Log out")
                }
            }
        }

        is UsersViewState.Empty -> {
           Text(text = "Empty")
        }
    }


}

//    when (val state = usersViewState) {
//        is UsersViewState.Loading -> LoadingScreen()
//
//        is UsersViewState.Empty -> Text(text = "No user data available")
//
//        is UsersViewState.Error -> Text(text = "Error loading user data: ${state.exception.message}")
//
//        is UsersViewState.Success -> {
//            // Hiển thị thông tin người dùng
//            val userData = state.data.firstOrNull()
//            userData?.let {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Image(
//                        painter = rememberAsyncImagePainter(model = user.imageUrl),
//                        contentDescription = "Profile Image",
//                        modifier = Modifier
//                            .size(120.dp)
//                            .padding(8.dp)
//                    )
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                     Text(text = "it", fontSize = 24.sp) }// Replace with the user's name
//
//                    Spacer(modifier = Modifier.height(16.dp))
//
//                  Text(text = "it") }// Thay thế bằng email của người dùng
//
//                    Spacer(modifier = Modifier.height(24.dp))
//
//                    Button(
//                        onClick = { actions.gotoEditProfile() },
//                        colors = ButtonDefaults.buttonColors(Color.White),
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Icon(
//                            Icons.Filled.Settings,
//                            contentDescription = "Edit Profile"
//                        ) // Icon chỉnh sửa thông tin cá nhân
//                        Spacer(modifier = Modifier.width(8.dp))
//                        Text(text = "Chỉnh sửa thông tin cá nhân")
//                    }
//
//                    Spacer(modifier = Modifier.height(24.dp))
//
//                    Button(
//                        onClick = {
//                            viewModel.logOut()
//                            actions.gotoLogOut()
//                        },
//                        colors = ButtonDefaults.buttonColors(Color.White),
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(text = "Đăng xuất")
//                    }
//                }
//
//
//            }
//
//        }
//    }
//}



