package com.example.eshopz

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductPage(navController: NavController, dataViewModel: DataViewModel, searchText: String) {
    var searchResults by remember { mutableStateOf(emptyList<Items>()) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = searchText,
                onValueChange = {  },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle.Default,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = {
                    navController.navigate(NavigationController.ProductPage.withArgs(searchText))
                }),
                leadingIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }

        Text(text = "Legend")

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(searchResults) { result ->
                if (searchResults.isEmpty()){
                    Text("No result found")
                }
                else if (result.Name != null &&
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
                    result.Stock != null ) {

                    val imageURL = result.ImageURL

                    ProductCard(
                        Name = result.Name,
                        Price = result.Price,
                        Rating = result.Rating,
                        ImageURL = imageURL,
                        Seller = result.Seller,
                        Free_shipping = result.Free_shipping,
                        Meetup = result.Meetup,
                        Condition = result.Condition,
                        Location = result.Location,
                        Description = result.Description,
                        UID = result.UID,
                        Category = result.Category,
                        Sales = result.Sales,
                        Stock = result.Stock,
                        modifier = Modifier.fillMaxHeight(0.5f).fillMaxWidth(0.4f)
                            .padding(8.dp),
                        navController = navController
                    )
                }
                else {
                    Text("No result found / corrupted data")
                }
            }
        }
    }

    // Launch coroutine when searchText changes
    LaunchedEffect(searchText) {
        if (searchText.isNotEmpty()) {
            scope.launch {
                val results = dataViewModel.searchData(name = searchText)
                Log.d("SearchData", "Results: $results")
                searchResults = results
            }
        } else {
            searchResults = emptyList() // Clear search results if searchText is empty
        }
    }
}

