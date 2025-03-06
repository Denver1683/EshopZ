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
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth

private var ItemList = mutableListOf<Items>()
private var QuantityList = mutableListOf<Int>()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartPage(navController: NavController, dataViewModel: DataViewModel, name: String?) {
    val notification = rememberSaveable{
        mutableStateOf("")
    }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }
    var searchResults by remember { mutableStateOf(emptyList<Cart>()) }
    var cartList by remember { mutableStateOf(emptyList<Items>()) }
    var total by remember { mutableStateOf(0.0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Cart") },
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
                        navController.navigate(NavigationController.WishlistPage.withArgs(name))
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
            BottomAppBar() {
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    Text(text = "Total: RM $total", fontSize = 16.sp,
                        textAlign = TextAlign.Start)
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { /*TODO*/
                                        Log.d("CartPage", "ItemList: $ItemList")
                                        Log.d("CartPage", "QuantityList: $QuantityList")
                                        Checkout(email = name!!, itemList = ItemList, quantityList= QuantityList)
                                        navController.navigate(NavigationController.CartPage.withArgs(name))
                                        notification.value = "Thank you for buying in EshopZ"
                                     }, modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(text = "Checkout",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 16.sp)
                    }
                }
            }

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
            if (cartList.isEmpty() || searchResults.isEmpty()) {
                Text(text = "Your Cart is Empty")
            } else {
                LazyColumn {
                    items(searchResults) { result ->
                        val item = remember { mutableStateOf(Items()) }
                        LaunchedEffect(result.UID) {
                            item.value = dataViewModel.FindbyUID(result.UID!!)
                        }
                        val quantity = remember { mutableStateOf(result.Sum!!) }
                        if ((item.value.Name != "" && item.value.ImageURL != "")){
                            ItemList.add(item.value)
                            QuantityList.add(result.Sum!!)
                            CartCard(
                                navController = navController,
                                item = item.value,
                                quantity = quantity.value,
                                onQuantityDecreased = {
                                    val updatedQuantity = quantity.value - 1
                                    if (updatedQuantity > 0) {
                                        quantity.value = updatedQuantity
                                        AddToCart(
                                            email = FirebaseAuth.getInstance().currentUser!!.email.toString(),
                                            UID = result.UID!!,
                                            Sum = updatedQuantity
                                        )
                                        total = updateTotal(cartList, searchResults)
                                        navController.navigate(
                                            NavigationController.CartPage.withArgs(
                                                name
                                            )
                                        )
                                    } else {
                                        RemoveFromCart(
                                            email = FirebaseAuth.getInstance().currentUser!!.email.toString(),
                                            UID = result.UID!!
                                        )
                                        total = updateTotal(cartList, searchResults)
                                        navController.navigate(
                                            NavigationController.CartPage.withArgs(
                                                name
                                            )
                                        )
                                    }
                                },
                                onQuantityIncreased = {
                                    if (quantity.value + 1 <= item.value.Stock!!) {
                                        val updatedQuantity = quantity.value + 1
                                        quantity.value = updatedQuantity
                                        AddToCart(
                                            email = FirebaseAuth.getInstance().currentUser!!.email.toString(),
                                            UID = result.UID!!,
                                            Sum = updatedQuantity
                                        )
                                        total = updateTotal(cartList, searchResults)
                                        navController.navigate(
                                            NavigationController.CartPage.withArgs(
                                                name
                                            )
                                        )
                                    } else {
                                        notification.value =
                                            "You've reached the maximum amount you can buy"
                                    }
                                },
                                onRemoveItem = {
                                    RemoveFromCart(
                                        email = FirebaseAuth.getInstance().currentUser!!.email.toString(),
                                        UID = result.UID!!
                                    )
                                    total = updateTotal(cartList, searchResults)
                                    navController.navigate(
                                        NavigationController.CartPage.withArgs(
                                            name
                                        )
                                    )
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
            val results = dataViewModel.LoadCart(name = name)
            Log.d("SearchData", "Results: $results")
            if (results != null) {
                searchResults = results // Filter out null values
                cartList = searchResults.mapNotNull { cart ->
                    dataViewModel.FindbyUID(cart.UID!!)
                }
                total = updateTotal(cartList, searchResults)
            } else {
                searchResults = emptyList()
                cartList = emptyList()
                total = 0.0
            }
        }
    }


}

private fun updateTotal(cartList: List<Items>, searchResults: List<Cart>): Double {
    var total = 0.0
    for (item in cartList) {
        val cartItem = searchResults.find { cart -> cart.UID == item.UID }
        if (cartItem != null) {
            total += (item.Price ?: 0.0) * (cartItem.Sum ?: 0)
        }
    }
    return total
}