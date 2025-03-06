package com.example.eshopz

import MainMenu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth


@Composable
fun Navigation(auth: FirebaseAuth) {
    val navController = rememberNavController()

    // Check if the user is already logged in
    val user = auth.currentUser

    LaunchedEffect(user) {
        if (user != null) {
            // User is logged in, navigate to HomePage
            navController.navigate(NavigationController.HomePage.route + "/${user.email}")
        } else {
            // User is not logged in, navigate to LoginPage
            navController.navigate(NavigationController.LoginPage.route)
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavigationController.LoginPage.route
    ) {
        composable(route = NavigationController.LoginPage.route) {
            LoginScreen(navController = navController, auth)
        }
        composable(
            route = NavigationController.HomePage.route + "/{Email}",
            arguments = listOf(
                navArgument("Email") {
                    type = NavType.StringType
                    defaultValue = "Unknown User"
                    nullable = true
                }
            )
        ) { entry ->
            MainMenu(navController = navController, name = entry.arguments?.getString("Email"))
        }
        composable(
            route = NavigationController.ProfilePage.route + "/{Email}",
            arguments = listOf(
                    navArgument("Email"){
                        type = NavType.StringType
                        defaultValue = "Unknown User"
                        nullable = true
                    }
            )
        ){entry->
            ProfileScreen(navController = navController, name = entry.arguments?.getString("Email"))
        }
        composable(
            route= NavigationController.AddItemPage.route + "/{Email}",
            arguments = listOf(
                navArgument("Email"){
                    type = NavType.StringType
                    defaultValue = "Unknown User"
                    nullable = true
                }
        )){entry ->
            AddItemScreen(navController = navController, name = entry.arguments?.getString("Email"))
        }
        composable(
            route = NavigationController.ProductPage.route + "/{searchText}",
            arguments = listOf(
                navArgument("searchText"){
                    type = NavType.StringType
                    defaultValue = "Unknown Item"
                    nullable = true
                }
            )
        ) { entry ->
            val dataViewModel: DataViewModel = viewModel() // Instantiate your DataViewModel here
            ProductPage(navController = navController, dataViewModel = dataViewModel, searchText = entry.arguments?.getString("searchText")!!)
        }
        composable(
            route = NavigationController.SellerPage.route + "/{Email}",
            arguments = listOf(
                navArgument("Email"){
                    type = NavType.StringType
                    defaultValue = "Unknown User"
                    nullable = true
                }
            )
        ){entry->
            val dataViewModel: DataViewModel = viewModel()
            SellerPage(navController = navController, dataViewModel = dataViewModel, name = entry.arguments?.getString("Email"))
        }
        composable(
            route = NavigationController.CartPage.route + "/{Email}",
            arguments = listOf(
                navArgument("Email"){
                    type = NavType.StringType
                    defaultValue = "Unknown User"
                    nullable = true
                }
            )
        ){entry->
            val dataViewModel: DataViewModel = viewModel()
            CartPage(navController = navController, dataViewModel = dataViewModel,name = entry.arguments?.getString("Email"))
        }
        composable(
            route = NavigationController.ProductDetails.route + "/{itemID}",
            arguments = listOf(
                navArgument("itemID") {
                    type = NavType.StringType
                    defaultValue = "Unknown UID"
                    nullable = true
                }
            )
        ) { entry ->
            val dataViewModel: DataViewModel = viewModel()
            ProductDetail(navController = navController, dataViewModel = dataViewModel, itemID = entry.arguments?.getString("itemID")!!)
        }
        composable(
            route = NavigationController.WishlistPage.route + "/{Email}",
            arguments = listOf(
                navArgument("Email"){
                    type = NavType.StringType
                    defaultValue = "Unknown User"
                    nullable = true
                }
            )
        ){entry->
            val dataViewModel: DataViewModel = viewModel()
            WishlistPage(navController = navController, dataViewModel = dataViewModel,name = entry.arguments?.getString("Email"))
        }
        composable(
            route = NavigationController.PurchasedPage.route + "/{Email}",
            arguments = listOf(
                navArgument("Email"){
                    type = NavType.StringType
                    defaultValue = "Unknown User"
                    nullable = true
                }
            )
        ){entry->
            val dataViewModel: DataViewModel = viewModel()
            PurchasedPage(navController = navController, dataViewModel = dataViewModel,name = entry.arguments?.getString("Email"))
        }
    }
}
