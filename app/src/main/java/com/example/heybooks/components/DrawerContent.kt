package com.example.heybooks.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.MainViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TopBarAdmin(viewModel: MainViewModel, actions: MainActions){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                backgroundColor = MaterialTheme.colors.primary,
                actions = {
                    IconButton(onClick = { actions.gotoLogOut() }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
                    }
                }
            )
        },
        drawerContent = {
            DrawerContent(viewModel, actions)
        },
        content = {
//            AdminContent(viewModel, actions)
        }
    )
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerContent(viewModel: MainViewModel, actions: MainActions) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Admin Menu",
            fontSize = 20.sp,
            modifier = Modifier.padding(16.dp),
        )
        ListItem(
            text = { Text("Book List") },
            icon = { Icon(Icons.Filled.Book, contentDescription = null) },
            modifier = Modifier.clickable(onClick = { actions.gotoAdminBookList() })
        )
        ListItem(
            text = { Text("Add Book") },
            icon = { Icon(Icons.Filled.Add, contentDescription = null) },
            modifier = Modifier.clickable(onClick = { actions.gotoAddBook() })
        )

    }
}
@Composable
fun AdminContent(viewModel: MainViewModel, actions: MainActions) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Welcome to the Admin Dashboard",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}