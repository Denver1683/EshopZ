package com.example.eshopz

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eshopz.ui.theme.EshopZTheme
import kotlinx.coroutines.launch
import java.util.UUID


@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(navController: NavController, name: String?){

    var Seller: String = name!!

    var Category by rememberSaveable{
        mutableStateOf("")
    }
    var Location by rememberSaveable{
        mutableStateOf("")
    }
    var Condition by rememberSaveable{
        mutableStateOf("")
    }
    var Description by rememberSaveable{
        mutableStateOf("")
    }
    var Name by rememberSaveable{
        mutableStateOf("")
    }
    var Price by rememberSaveable {
        mutableStateOf<Double?>(null)
    }
    var Stock by rememberSaveable {
        mutableStateOf<Int?>(null)
    }
    var Rating by rememberSaveable{
        mutableStateOf(0.00)
    }
    var Sales by rememberSaveable{
        mutableStateOf(0)
    }
    var Free_shipping by rememberSaveable { mutableStateOf(true) }
    var Meetup by rememberSaveable { mutableStateOf(true) }


    var imgbitmap:ByteArray = byteArrayOf()
    val coroutineScope = rememberCoroutineScope()
    val notification = rememberSaveable{
        mutableStateOf("")
    }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    Column(modifier = Modifier
        .verticalScroll(rememberScrollState())
        .padding(8.dp)){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Cancel", modifier = Modifier.clickable {
                navController.popBackStack()
                notification.value = "Cancelled" })
            Text(text = "Save",
                modifier = Modifier.clickable {
                    coroutineScope.launch {
                        val ImageURL = uploadImageToFirebaseStorage(imgbitmap, UUID.randomUUID().toString())
                        println(ImageURL)
                        if (Price != null && Stock != null && Category != null && Condition != null && Description != null && Free_shipping != null &&
                            ImageURL != null && Name != null && Location != null) {
                            UpdateToDatabase(
                                Category,
                                Condition,
                                Description,
                                Free_shipping,
                                ImageURL,
                                Meetup,
                                Name,
                                Price!!,
                                Rating,
                                Sales,
                                Seller,
                                Stock!!,
                                Location
                            )
                            notification.value = "Product Sucessfully Updated"
                            navController.popBackStack()
                        }
                        else{
                            notification.value = "Please fill all columns"
                        }
                    }
                })
        }

        val context = LocalContext.current
        val Bitmap = remember { mutableStateOf<ImageBitmap?>(null) }
        val imageUri = rememberSaveable { mutableStateOf("") }

        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
                uri: Uri? ->
            uri?.let {imageUri.value = it.toString()}
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            Bitmap.value = bitmap.asImageBitmap()
        }

        Column(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){

            if (Bitmap.value != null) {
                Image(
                    bitmap = Bitmap.value!!,
                    contentDescription = "Selected Image",
                    modifier = Modifier.clickable { /* Handle image click here */ }
                )
                imgbitmap = convertImageBitmapToByteArray(Bitmap.value!!)
            } else {
                Text("No image selected")
            }

            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Select Image")
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text="Category", modifier = Modifier.width(100.dp))
            TextField(value = Category,
                onValueChange = { Category = it },
                colors = TextFieldDefaults.textFieldColors(
                    //backgroundColor= Color.Transparent,
                    textColor = Color.Black
                ))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text="Condition", modifier = Modifier.width(100.dp))
            TextField(value = Condition,
                onValueChange = { Condition = it },
                colors = TextFieldDefaults.textFieldColors(
                    //backgroundColor= Color.Transparent,
                    textColor = Color.Black
                ))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text="Description", modifier = Modifier.width(100.dp))
            TextField(value = Description,
                onValueChange = { Description = it },
                colors = TextFieldDefaults.textFieldColors(
                    //backgroundColor= Color.Transparent,
                    textColor = Color.Black
                ))
        }
        //Free Shipping Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Free Shipping", modifier = Modifier.width(100.dp))
            Checkbox(
                checked = Free_shipping,
                onCheckedChange = { Free_shipping = it },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = Color.White
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Meetup", modifier = Modifier.width(100.dp))
            Checkbox(
                checked = Meetup,
                onCheckedChange = { Meetup = it },
                colors = CheckboxDefaults.colors(
                    checkmarkColor = Color.White
                )
            )
        }
        //Meetup Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text="Name", modifier = Modifier.width(100.dp))
            TextField(value = Name,
                onValueChange = { Name = it },
                colors = TextFieldDefaults.textFieldColors(
                    //backgroundColor= Color.Transparent,
                    textColor = Color.Black
                ))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text="Price", modifier = Modifier.width(100.dp))
            TextField(
                value = Price?.toString() ?: "",
                onValueChange = { newValue ->
                    Price = newValue.toDoubleOrNull()
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black
                )
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text="Stock", modifier = Modifier.width(100.dp))
            TextField(
                value = Stock?.toString() ?: "",
                onValueChange = { newValue ->
                    Stock = newValue.toIntOrNull()
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black
                )
            )

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text="Location", modifier = Modifier.width(100.dp))
            TextField(value = Location,
                onValueChange = {Location = it },
                colors = TextFieldDefaults.textFieldColors(
                    //backgroundColor= Color.Transparent,
                    textColor = Color.Black
                ))
        }
    }

}


@Preview(showBackground = true)
@Composable
fun AddItemPreview() {
    EshopZTheme {
        //AddItemScreen()
    }
}