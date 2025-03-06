package com.example.eshopz

import android.annotation.SuppressLint
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, auth: FirebaseAuth){
    val notification = rememberSaveable {
        mutableStateOf("")
    }
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }
    val focusManager = LocalFocusManager.current

    val buttonColors = ButtonDefaults.buttonColors(
        contentColor = Color.White
    )


    var email by remember{
        mutableStateOf("")
    }
    var password by remember{
        mutableStateOf("")
    }

    val isEmailValid by derivedStateOf {
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    val isPasswordValid by derivedStateOf {
        password.length > 8
    }

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier
            .background(color = Color.LightGray)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top

    ) {
        Image(painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Image", modifier = Modifier.size(150.dp))

        Card(modifier = Modifier
            .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.Black)
        )
        {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(all = 10.dp)
            ) {
                OutlinedTextField(value = email,
                    onValueChange = {email = it},
                    label = { Text("Email Address") },
                    placeholder = { Text("myemail@email.com") },
                    singleLine = true ,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {focusManager.moveFocus(FocusDirection.Down)}
                    ),
                    isError = !isEmailValid,
                    trailingIcon = {
                        if(email.isNotBlank()){
                            IconButton(onClick = { email = ""}){
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear Email"
                                )
                            }
                        }
                    }
                )

                OutlinedTextField(value = password,
                    onValueChange = {password = it},
                    label = { Text("Password") },
                    singleLine = true ,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            auth.signInWithEmailAndPassword(email,password)
                                .addOnCompleteListener{
                                    if (it.isSuccessful){
                                        Log.d(MainActivity.TAG, "The user has successfully logged in")
                                        notification.value = "Logged in successfully"
                                        val user = auth.currentUser
                                        if (user != null) {
                                            navController.navigate(NavigationController.HomePage.withArgs(user.email.toString()))
                                        }
                                    }
                                    else{
                                        Log.w(MainActivity.TAG, "The user has failed to log in", it.exception)
                                        notification.value = "Please recheck username/password or sign up if you haven't"
                                    }
                                }
                        }
                    ),
                    isError = !isPasswordValid,
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible}){
                            Icon(
                                imageVector = if(isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Password Visibility Toggle"
                            )
                        }
                    },
                    visualTransformation = if(isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )

                Button(onClick = {
                    auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener{
                            if (it.isSuccessful){
                                Log.d(MainActivity.TAG, "The user has successfully logged in")
                                notification.value = "Logged in successfully"

                                val user = auth.currentUser
                                if (user != null) {
                                    navController.navigate(NavigationController.HomePage.withArgs(user.email.toString()))
                                }
                            }
                            else{
                                Log.w(MainActivity.TAG, "The user has failed to log in", it.exception)
                                notification.value = "Please recheck username/password or sign up if you haven't"
                            }
                        }
                },
                    modifier = Modifier.fillMaxWidth(),
                    colors = buttonColors,
                ){
                    Text(text = "Log in",
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp)
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ){
            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    color = Color.Blue,
                    fontStyle = FontStyle.Italic,
                    text = "Forgot Password?",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }

        Button(
            onClick = {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(MainActivity.TAG, "createUserWithEmail:success")
                            notification.value = "Registered successfully! Welcome to EshopZ"
                            val user = auth.currentUser
                            if (user != null) {
                                navController.navigate(NavigationController.HomePage.withArgs(user.email.toString()))
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(MainActivity.TAG, "createUserWithEmail:failure", task.exception)
                            notification.value = "Email has been registered before, if you forgot your password please tap forgot password,"
                        }
                    }
            },
            enabled = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            colors = buttonColors
        ) {
            Text(
                text = "Register",
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}