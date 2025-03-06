@file:Suppress("DEPRECATION")

package com.example.eshopz

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.eshopz.ui.theme.EshopZTheme
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, name: String?) {

    val notification = rememberSaveable {
        mutableStateOf("")
    }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }
    val imgbitmap:ByteArray = byteArrayOf()
    val coroutineScope = rememberCoroutineScope()

    var username by rememberSaveable {
        mutableStateOf(FirebaseAuth.getInstance().currentUser!!.email.toString())
    }
    var password by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Cancel",
                modifier = Modifier.clickable {
                    navController.popBackStack()
                }
            )
            Text(
                text = "Save",
                modifier = Modifier.clickable {
                    coroutineScope.launch {
                        val ImageURL = uploadImageToFirebaseStorage(imgbitmap, UUID.randomUUID().toString())
                        SetProfileImage(email = name!!, ImageURL=ImageURL)
                    }
                    if (password != ""){
                        changePassword(password)
                    }
                    notification.value = "Profile Updated"
                    navController.navigate(NavigationController.HomePage.withArgs(name))
                }
            )
        }

        ProfileImage(name)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Username", modifier = Modifier.weight(0.2f))
            TextField(
                value = username,
                onValueChange = { username = it },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black
                ),
                modifier = Modifier.weight(0.8f),
                enabled = false
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Password", modifier = Modifier.weight(0.2f))
            TextField(
                value = password,
                onValueChange = { password = it },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Black
                ),
                modifier = Modifier.weight(0.8f)
            )
        }

        Button(
            onClick = {
                FirebaseAuth.getInstance().signOut()
                // Navigate to the login screen after logout
                navController.navigate(NavigationController.LoginPage.route)
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Log out")
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProfileImage(name: String?){
    val imageUri = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val dataViewModel: DataViewModel = viewModel()
    LaunchedEffect(name) {
        if (name != null) {
            scope.launch {
                val results = dataViewModel.profileData(name)
                Log.d("Profile Image URL", "Results: $results")
                if (results.value != "")
                    imageUri.value = results.value
            }
        } else {
            imageUri.value = ""
        }
    }
    val painter = rememberImagePainter(
        if (imageUri.value == "")
            R.drawable.ic_user
        else
            imageUri.value
    )

    val context = LocalContext.current
    val Bitmap = remember { mutableStateOf<ImageBitmap?>(null) }
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
        Card(shape = CircleShape,
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp)
        ){
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .wrapContentSize()
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop)
        }
        Text(text="Change Profile Picture")
    }
}

private fun changePassword(newPassword: String) {
    val user = FirebaseAuth.getInstance().currentUser
    user?.updatePassword(newPassword)
        ?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Password change was successful
                Log.d("ProfileScreen", "Password updated successfully")
            } else {
                // Password change failed
                Log.e("ProfileScreen", "Failed to update password: ${task.exception}")
            }
        }
}

@Preview(showBackground = true)
@Composable
fun ProfilePagePreview() {
    EshopZTheme {

    }
}


