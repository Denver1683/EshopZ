
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.eshopz.MainViewModel
import com.example.eshopz.MultiToggleButton
import com.example.eshopz.NavigationController
import com.example.eshopz.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(navController: NavController, name: String?) {
    val viewModel = viewModel<MainViewModel>()
    val searchText by viewModel.searchText.collectAsState()

    val painter = rememberImagePainter(R.drawable.ic_user)

    val selectedButtons = remember { mutableStateListOf(0) }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    IconButton(onClick = {
                        navController.navigate(NavigationController.ProfilePage.withArgs(name))
                    }) {
                        Icon(painter = painter, contentDescription = "Profile Image")
                    }
                }
                Text(text = "Hello, $name", fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {
                        navController.navigate(NavigationController.CartPage.withArgs(name))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart"
                    )
                }
                IconButton(
                    onClick = {
                        navController.navigate(NavigationController.WishlistPage.withArgs(name))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Heart"
                    )
                }
                IconButton(
                    onClick = {
                        navController.navigate(NavigationController.PurchasedPage.withArgs(name))
                    },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shop,
                        contentDescription = "Shop"
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Column {
                    Text(text = "e-Wallet")
                    Text(text = "COMING SOON")
                }
            }
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Image",
                modifier = Modifier.size(150.dp)
            )
            Text(text = "Your Convenient Online Shopping Partner")
            Text(text = "Legend")
            TextField(
                value = searchText,
                onValueChange = viewModel::onSearchTextChange,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 50.dp),
                singleLine = true,
                placeholder = { Text(text = "Search the product you want") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        /* SEARCH PRODUCT */
                        if (searchText != "") {
                            navController.navigate(
                                NavigationController.ProductPage.withArgs(
                                    searchText
                                )
                            )
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Item"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(320.dp))
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    Text("You're a:")
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp).padding(horizontal = 71.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        MultiToggleButton(
                            text = "Buyer",
                            selected = selectedButtons.contains(0),
                            onClick = {
                                if (!selectedButtons.contains(0)) {
                                    selectedButtons.clear()
                                    selectedButtons.add(0)
                                    navController.navigate(NavigationController.HomePage.withArgs(name))
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
                                    navController.navigate(NavigationController.SellerPage.withArgs(name))
                                }
                            }
                        )
                    }
                }
            }
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


