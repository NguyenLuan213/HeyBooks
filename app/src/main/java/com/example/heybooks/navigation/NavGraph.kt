package com.example.heybooks.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.heybooks.components.BottomNavigationContent
import com.example.heybooks.view.BookDetailsScreen
import com.example.heybooks.view.LoginScreen
import com.example.heybooks.view.ProfileScreen
import com.example.heybooks.view.SavedBookScreen
import com.example.heybooks.view.SignUpScreen
import com.example.heybooks.viewmodel.MainViewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.heybooks.view.AdminHomeScreen
import com.example.heybooks.view.BookListScreen
import com.example.heybooks.view.EditProfileScreen
import com.example.heybooks.view.ReadingScreen
import com.example.heybooks.view.ReviewScreen

object EndPoints {
    const val ID = "id"
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val actions = remember(navController) { MainActions(navController) }
    val context = LocalContext.current

    val bottomNavigationItems = listOf(
        Screen.BookList,
        Screen.Saved,
        Screen.Profile
    )

    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }

    NavHost(navController, startDestination = Screen.Login.route) {

        composable(
            route = "${Screen.Reading.route}/{bookContent}",
            arguments = listOf(navArgument("bookContent") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val viewModel = hiltViewModel<MainViewModel>()
            val bookContent = navBackStackEntry.arguments?.getString("bookContent")
            if (bookContent != null) {
                ReadingScreen(viewModel, actions, bookContent)
                BottomNavigationContent(viewModel, actions, -1)
            } else {
                BottomNavigationContent(viewModel, actions, -1)
            }
        }




        composable(Screen.BookList.route) {
            val viewModel: MainViewModel = viewModel(
                factory = HiltViewModelFactory(LocalContext.current, it)
            )
            BookListScreen(viewModel, actions)
        }
        composable(Screen.Admin.route) {
            val viewModel: MainViewModel = viewModel(
                factory = HiltViewModelFactory(LocalContext.current, it)
            )
            AdminHomeScreen(viewModel, actions)
        }

        composable(Screen.Login.route) {
            val viewModel: MainViewModel = viewModel(
                factory = HiltViewModelFactory(LocalContext.current, it)
            )
            LoginScreen(viewModel, actions)
        }

        composable(Screen.SignUp.route) {
            val viewModel: MainViewModel = viewModel(
                factory = HiltViewModelFactory(LocalContext.current, it)
            )
            SignUpScreen(viewModel, actions)
        }

        composable(Screen.LogOut.route) {
            val viewModel: MainViewModel = viewModel(
                factory = HiltViewModelFactory(LocalContext.current, it)
            )
            LoginScreen(viewModel, actions)
        }

        composable(Screen.EditProfile.route) {
            val viewModel: MainViewModel = viewModel(
                factory = HiltViewModelFactory(LocalContext.current, it)
            )
            EditProfileScreen(viewModel, actions)
            BottomNavigationContent(viewModel, actions, -1)
        }


        composable(
            "${Screen.Details.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val viewModel = hiltViewModel<MainViewModel>()
            val isbn = navBackStackEntry.arguments?.getString("id")
            BookDetailsScreen(viewModel, actions, isbn)

            BottomNavigationContent(viewModel, actions, -1)
        }


        // Content for each screen
        bottomNavigationItems.forEachIndexed { index, screen ->
            composable(screen.route) {
                val viewModel: MainViewModel = viewModel(
                    factory = HiltViewModelFactory(LocalContext.current, it)
                )
//                viewModel.getAllBooks()

                when (screen) {
                    Screen.BookList -> BookListScreen(viewModel, actions)
                    Screen.Saved -> SavedBookScreen(viewModel, actions)
                    Screen.Profile -> ProfileScreen(viewModel, actions)
                    else -> { /* Handle other screens if needed */
                    }
                }
                BottomNavigationContent(viewModel, actions, index)
            }
        }


    }
}


class MainActions(navController: NavController) {

    val upPress: () -> Unit = {
        navController.navigateUp()
    }

    val gotoBookDetails: (String) -> Unit = { isbnNo ->
        navController.navigate("${Screen.Details.route}/$isbnNo")
    }

    val gotoBookList: () -> Unit = {
        navController.navigate(Screen.BookList.route)
    }

    val gotoLogin: () -> Unit = {
        navController.navigate(Screen.Login.route)
        {
            popUpTo(Screen.Login.route) { inclusive = true }
        }
    }

    val gotoSignUp: () -> Unit = {
        navController.navigate(Screen.SignUp.route)
    }

    val gotoLogOut: () -> Unit = {
        navController.navigate(Screen.Login.route)
    }

    val gotoSavedBook: () -> Unit = {
        navController.navigate(Screen.Saved.route)
    }
    val gotoProfile: () -> Unit = {
        navController.navigate(Screen.Profile.route)
    }
    val gotoEditProfile: () -> Unit = {
        navController.navigate(Screen.EditProfile.route)
    }
    val gotoAdmin: () -> Unit = {
        navController.navigate(Screen.Admin.route)
    }
    val gotoBookReading: (String) -> Unit = { bookContent ->
        navController.navigate("${Screen.Reading.route}/$bookContent")
    }


}