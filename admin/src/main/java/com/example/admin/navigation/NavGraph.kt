package com.example.heybooks.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.admin.view.HomeScreen
import com.example.heybooks.viewmodel.MainViewModel

object EndPoints {
    const val ID = "id"
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }
    val context = LocalContext.current

//    val bottomNavigationItems = listOf(
//        Screen.BookList,
//        Screen.Saved,
//        Screen.Profile
//    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavHost(navController, startDestination = Screen.HomeScreen.route) {


        composable(Screen.HomeScreen.route) {
            val viewModel: MainViewModel = viewModel(
                factory = HiltViewModelFactory(LocalContext.current, it)
            )
            HomeScreen(viewModel, actions)
        }




    }
}


class MainActions(navController: NavController) {

    val upPress: () -> Unit = {
        navController.navigateUp()
    }


    val gotoHome: () -> Unit = {
        navController.navigate(Screen.HomeScreen.route)
    }

}