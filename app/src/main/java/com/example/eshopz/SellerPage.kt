package com.example.eshopz

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerPage(navController: NavController, dataViewModel: DataViewModel, name: String?) {
    var searchResults by remember { mutableStateOf(emptyList<Items>()) }
    val scope = rememberCoroutineScope()
    var salesList by remember { mutableStateOf(emptyList<Sales>()) }
    val imageUri = rememberSaveable { mutableStateOf("") }
    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.ic_user
        else
            imageUri.value
    )
    val selectedButtons = remember { mutableStateListOf(1) }
    val notification = rememberSaveable {
        mutableStateOf("")
    }
    val isListingPage = remember { mutableStateOf(true) } // Track whether the current page is the listing page

    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding()
                    ) {
                        IconButton(
                            onClick = {
                                navController.navigate(NavigationController.ProfilePage.withArgs(name))
                            },
                        ) {
                            Icon(painter = painter, contentDescription = "Profile Image")
                        }
                        Text(text = "Hello, $name", fontSize = 16.sp)
                    }
                },
                actions = {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        IconButton(
                            onClick = {
                                isListingPage.value = !isListingPage.value
                            }
                        ) {
                            Text(
                                text = if (isListingPage.value) "Sales >" else "Listing >", fontSize = 12.sp
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        MultiToggleButton(
                            text = "Buyer",
                            selected = selectedButtons.contains(0),
                            onClick = {
                                if (!selectedButtons.contains(0)) {
                                    selectedButtons.clear()
                                    selectedButtons.add(0)
                                    navController.navigate(
                                        NavigationController.HomePage.withArgs(
                                            name
                                        )
                                    )
                                }
                            }
                        )
                        MultiToggleButton(
                            text = "Seller",
                            selected = selectedButtons.contains(1),
                            onClick = {
                                if (!selectedButtons.contains(1)) {
                                    selectedButtons.clear()
                                    selectedButtons.add(1)
                                    navController.navigate(
                                        NavigationController.SellerPage.withArgs(
                                            name
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.fillMaxSize().padding(start = 25.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(NavigationController.AddItemPage.withArgs(name))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White
                    )
                }
            }
        }
    ) { innerPadding ->
        if (isListingPage.value) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(searchResults) { result ->
                    if (
                        result.Name != null &&
                        result.Category != null &&
                        result.Price != null &&
                        result.Rating != null &&
                        result.ImageURL != null &&
                        result.Seller != null &&
                        result.Free_shipping != null &&
                        result.Meetup != null &&
                        result.Condition != null &&
                        result.Location != null &&
                        result.Description != null &&
                        result.UID != null &&
                        result.Sales != null &&
                        result.Stock != null
                    ) {
                        val quantity = remember { mutableStateOf(result.Stock) }
                        SellerCard(
                            navController = navController,
                            item = result,
                            quantity = quantity.value,
                            onQuantityDecreased = {
                                val updatedQuantity = quantity.value - 1
                                if (updatedQuantity >= 0) {
                                    quantity.value = updatedQuantity
                                    EditItem(
                                        result.Category,
                                        result.Condition,
                                        result.Description,
                                        result.Free_shipping,
                                        result.ImageURL,
                                        result.Meetup,
                                        result.Name,
                                        result.Price,
                                        result.Rating,
                                        result.Sales,
                                        result.Seller,
                                        updatedQuantity,
                                        result.Location,
                                        result.UID
                                    )
                                    notification.value = "Product Successfully Updated"
                                    navController.navigate(NavigationController.SellerPage.withArgs(name))
                                } else {
                                    RemoveListing(UID = result.UID)
                                    navController.navigate(NavigationController.SellerPage.withArgs(name))
                                }
                            },
                            onQuantityIncreased = {
                                val updatedQuantity = quantity.value + 1
                                quantity.value = updatedQuantity
                                EditItem(
                                    result.Category,
                                    result.Condition,
                                    result.Description,
                                    result.Free_shipping,
                                    result.ImageURL,
                                    result.Meetup,
                                    result.Name,
                                    result.Price,
                                    result.Rating,
                                    result.Sales,
                                    result.Seller,
                                    updatedQuantity,
                                    result.Location,
                                    result.UID
                                )
                                notification.value = "Product Successfully Updated"
                                navController.navigate(NavigationController.SellerPage.withArgs(name))
                            },
                            onRemoveItem = {
                                RemoveListing(UID = result.UID)
                                navController.navigate(NavigationController.SellerPage.withArgs(name))
                            }
                        )
                    } else {
                        Text("Let's add your first product")
                    }
                    this@LazyColumn.item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        } else {
            // Sales Page
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(salesList) { result ->
                    if (
                        result.Name != null &&
                        result.Category != null &&
                        result.Price != null &&
                        result.Rating != null &&
                        result.ImageURL != null &&
                        result.Seller != null &&
                        result.Free_shipping != null &&
                        result.Meetup != null &&
                        result.Condition != null &&
                        result.Location != null &&
                        result.Description != null &&
                        result.UID != null &&
                        result.Sales != null &&
                        result.Stock != null &&
                        result.Buyer != null &&
                        result.Quantity != null
                    ) {
                        val quantity = remember { mutableStateOf(result.Quantity) }
                        SalesCard(
                            navController = navController,
                            sales = result,
                            quantity = quantity.value,
                            onRemoveItem = {
                                name?.let { CancelOrder(it,result) }
                                navController.navigate(NavigationController.SellerPage.withArgs(name))
                            }
                        )
                    } else {
                        Text("You haven't got any new sales :(")
                    }
                    this@LazyColumn.item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }

    LaunchedEffect(name) {
        if (name != null) {
            scope.launch {
                val sales = dataViewModel.salesData(name= name)
                val results = dataViewModel.sellerItems(name = name)
                Log.d("SearchData", "Results: $results")
                searchResults = results
                salesList = sales
            }
        } else {
            searchResults = emptyList() // Clear search results if searchText is empty
            salesList = emptyList() // Clear sales results if no sales happened
        }
    }
}


@Composable
fun MultiToggleButton(text: String, selected: Boolean, onClick: () -> Unit) {

    val contentColor = if (selected) {
        Color.Red
    } else {
        Color.White
    }

    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(8.dp),
    ) {
        Text(
            text = text,
            color = contentColor
        )
    }
}