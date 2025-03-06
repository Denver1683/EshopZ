package com.example.eshopz

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.eshopz.ui.theme.EshopZTheme

@Composable
fun ProductCard(
    navController: NavController,
    Name:String,
    Price:Double,
    Rating:Double,
    ImageURL:String,
    Seller:String,
    Free_shipping:Boolean,
    Meetup:Boolean,
    Condition:String,
    Location:String,
    //Used later on product detail page
    Description: String,
    UID: String,
    Category: String,
    Sales: Int,
    Stock: Int,
    modifier: Modifier = Modifier
){
    Log.d("ProductCard", "ImageURL: $ImageURL") // Add this logging statement
    //val getData = dataViewModel.state.value
    Card(modifier = modifier.clickable{
        navController.navigate(NavigationController.ProductDetails.withArgs(UID))
    },
        shape = RoundedCornerShape(5.dp),
    ){
        Box(modifier = Modifier.height(300.dp)){
            Column(modifier = Modifier.padding(8.dp)){
                Image(painter = rememberAsyncImagePainter(model = ImageURL),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = modifier.fillMaxWidth(1f)
                        .padding(top = 10.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable{
                            navController.navigate(NavigationController.ProductDetails.withArgs(UID))
                        }
                )
                Text(text=Rating.toString(), style = TextStyle(color = Color.Black, fontSize = 8.sp))
                Text(text=Name, style = TextStyle(color = Color.Black, fontSize = 16.sp))
                Text(text=Condition, style = TextStyle(color = Color.Black, fontSize = 8.sp))
                if (Stock != 0) {
                    Text(
                        text = Price.toString(),
                        style = TextStyle(color = Color.Black, fontSize = 16.sp)
                    )
                }
                else{
                    Text(
                        text = "SOLD OUT!",
                        style = TextStyle(color = Color.Red, fontSize = 16.sp)
                    )
                }
                Text(text=Location, style = TextStyle(color = Color.Black, fontSize = 8.sp))
                Text(text=Seller, style = TextStyle(color = Color.Black, fontSize = 8.sp))
            }
            /*Box(modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
                contentAlignment = Alignment.BottomStart){

            }*/
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductCardPreview() {
    EshopZTheme {
        //ProductView()
    }
}