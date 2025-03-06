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
import androidx.compose.material.icons.filled.Clear
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
fun SalesCard(
    navController: NavController,
    sales: Sales,
    quantity: Int,
    onRemoveItem: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth().clickable{
                navController.navigate(NavigationController.ProductDetails.withArgs(sales.UID))
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
                    painter = rememberAsyncImagePainter(model = sales.ImageURL),
                    contentDescription = "Item Image",
                    modifier = Modifier.size(80.dp)
                )
                Column(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = sales.Name!!,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${sales.Condition}",
                        color = Color.Gray
                    )
                    Text(
                        text = "RM ${sales.Price}"
                    )
                    Text(
                        text = "Buyer: ${sales.Buyer}",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                    Text(
                        text = "Quantity: ${quantity}",
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

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
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Cancel Order"
                    )
                }
                Text(text="Cancel Order")
            }
        }
    }
}
