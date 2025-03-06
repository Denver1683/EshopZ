package com.example.eshopz


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@Composable
fun SellerCard(
    navController: NavController,
    item: Items,
    quantity: Int,
    onQuantityDecreased: () -> Unit,
    onQuantityIncreased: () -> Unit,
    onRemoveItem: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth().clickable{
                navController.navigate(NavigationController.ProductDetails.withArgs(item.UID))
            },
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = item.ImageURL),
                    contentDescription = "Item Image",
                    modifier = Modifier.size(80.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = item.Name!!,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${item.Condition}",
                        color = Color.Gray
                    )
                    Text(
                        text = "${item.Rating} / 5.0",
                        color = Color.Gray
                    )
                    Text(
                        text = "RM ${item.Price}"
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onQuantityDecreased) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease Quantity"
                            )
                        }
                        Text(
                            text = "${quantity}",
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(onClick = onQuantityIncreased) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase Quantity"
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                IconButton(onClick = onRemoveItem) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove Item"
                    )
                }
            }
        }
    }
}
