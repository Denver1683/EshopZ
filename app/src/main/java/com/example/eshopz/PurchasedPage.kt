package com.example.eshopz

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchasedPage(navController: NavController, dataViewModel: DataViewModel, name: String?) {
    var searchResults by remember { mutableStateOf(emptyList<Sales>()) }
    val scope = rememberCoroutineScope()
    var salesList by remember { mutableStateOf(emptyList<Sales>()) }
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
                title = { Text(text = "Purchased Page") },
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
                                text = if (isListingPage.value) "History >" else "On Going >", fontSize = 12.sp
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (isListingPage.value) {
            // On Going Purchase Page
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
                        result.Stock != null
                    ) {
                        val quantity = remember { mutableStateOf(result.Quantity) }
                        PurchasedCard(
                            navController = navController,
                            sales = result,
                            quantity = quantity.value,
                            onRemoveItem = {
                                name?.let { FinishOrder(email = it, sales = result) }
                                navController.navigate(NavigationController.PurchasedPage.withArgs(name))
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
        } else {
            //Purchase History Page
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
                        val quantity = remember { mutableStateOf(result.Quantity) }
                        PurchaseHistoryCard(
                            navController = navController,
                            sales = result,
                            quantity = quantity.value
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
                val sales = dataViewModel.purchaseData(name= name)
                val results = dataViewModel.purchaseHistoryData(name = name)
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