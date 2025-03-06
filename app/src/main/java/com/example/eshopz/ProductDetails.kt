package com.example.eshopz

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetail(navController: NavController, dataViewModel: DataViewModel, itemID : String) {
    var item by remember { mutableStateOf(Items()) }
    val notification = rememberSaveable{
        mutableStateOf("")
    }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    LaunchedEffect(itemID) {
        val results = dataViewModel.FindbyUID(itemID)
        item = results ?: Items()
        Log.d("SearchData", "Results: $results")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Product Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(NavigationController.CartPage.withArgs(FirebaseAuth.getInstance().currentUser!!.email.toString()))
                    }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart"
                        )
                    }
                    IconButton(onClick = {
                        navController.navigate(NavigationController.WishlistPage.withArgs(FirebaseAuth.getInstance().currentUser!!.email.toString()))
                    }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Heart"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Add to Cart button
                        Button(
                            onClick = {
                                if(item.Stock != 0){
                                    AddToCart(email= FirebaseAuth.getInstance().currentUser!!.email.toString(), UID= itemID, Sum= 1)
                                    notification.value = "Successfully added to cart"
                                }else{
                                    notification.value = "Sorry product has ran out of stock"
                                }
                                },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Add to Cart")
                        }

                        // Add to Wishlist button
                        Button(
                            onClick = {
                                    AddToWishlist(email= FirebaseAuth.getInstance().currentUser!!.email.toString(), UID= itemID)
                                    notification.value = "Successfully added to wishlist"
                                },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = "Add to Wishlist")
                        }
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp)
        ) {
            // Image
            Image(
                painter = rememberAsyncImagePainter(model = item.ImageURL),
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.FillWidth
            )

            // Title
            item.Name?.let { it1 ->
                Text(
                    text = it1,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // Price
            Text(
                text = "RM" + item.Price,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Rating
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Rating Icon",
                    modifier = Modifier.padding(end = 4.dp)
                )
                Text(
                    text = item.Rating.toString(),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = "(0 ratings)",
                    color = Color.Gray
                )
            }

            // Sales Amount
            Text(
                text = item.Sales.toString() + " Sold",
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Seller Name
            Text(
                text = "Seller: " + item.Seller,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Seller Location
            Text(
                text = "Location: " + item.Location,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Condition
            Text(
                text = "Condition: " + item.Condition,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Description
            Text(
                text = item.Description!!,
            )
        }




    }
}
