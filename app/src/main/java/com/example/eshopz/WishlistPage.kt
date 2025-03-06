package com.example.eshopz

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

private var ItemList = mutableStateListOf<Items>()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistPage(navController: NavController, dataViewModel: DataViewModel, name: String?) {
    val notification = rememberSaveable{
        mutableStateOf("")
    }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }
    var searchResults by remember { mutableStateOf(emptyList<String>()) }
    var cartList by remember { mutableStateOf(emptyList<Items>()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Wishlist") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(NavigationController.CartPage.withArgs(name))
                    }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Shopping Cart"
                        )
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(top = 60.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (searchResults.isEmpty()) {
                Text(text = "Your Wishlist is Empty")
            } else {
                LazyColumn {
                    items(searchResults) { result ->
                        val item = remember { mutableStateOf(Items()) }
                        LaunchedEffect(result) {
                            item.value = dataViewModel.FindbyUID(result)
                        }
                        if ((item.value.Name != "" && item.value.ImageURL != "")||(item.value.Name != null && item.value.ImageURL != null)){
                            ItemList.add(item.value)
                            WishlistCard(
                                navController = navController,
                                item = item.value,
                                onRemoveItem = {
                                    RemoveFromWishlist(email = FirebaseAuth.getInstance().currentUser!!.email.toString(), UID = result)
                                    navController.navigate(NavigationController.WishlistPage.withArgs(name))
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(name) {
        if (name != null) {
            val results = dataViewModel.LoadWishlist(name = name)
            Log.d("SearchData", "Results: $results")
            searchResults = results // Filter out null values
            cartList = searchResults.mapNotNull { UID ->
                dataViewModel.FindbyUID(UID)
            }
        }
    }


}
