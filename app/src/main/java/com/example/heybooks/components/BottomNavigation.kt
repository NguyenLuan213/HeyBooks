package com.example.heybooks.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.heybooks.navigation.MainActions
import com.example.heybooks.viewmodel.MainViewModel


data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val goto: () -> Unit

)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationContent(viewModel: MainViewModel, actions: MainActions, selectedIndex: Int) {
    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            goto = { actions.gotoBookList() }
        ),
        BottomNavigationItem(
            title = "Chat",
            selectedIcon = Icons.Filled.Bookmark,
            unselectedIcon = Icons.Filled.BookmarkBorder,
            goto = { actions.gotoSavedBook() }
        ),
        BottomNavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person,
            goto = { actions.gotoProfile() }
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(selectedIndex)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        NavigationBar(modifier = Modifier.align(Alignment.BottomCenter)) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = selectedItemIndex == index,
                    onClick = {
                        selectedItemIndex = index
                        item.goto() // Gọi hành động khi được chọn
                    },
//                    label = {
////                        Text(text = item.title)
//                    },
                    alwaysShowLabel = false,
                    icon = {

                        BadgedBox(
                            badge = {
                                // Thêm badge nếu cần
                            }
                        ) {
                            Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                ) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.selectedIcon
                                        }else if(index == -1){
                                            item.unselectedIcon
                                        } else item.unselectedIcon,
                                        contentDescription = item.title,

                                    )
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}
