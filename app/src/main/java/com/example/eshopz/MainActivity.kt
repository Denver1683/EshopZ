package com.example.eshopz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eshopz.ui.theme.EshopZTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {

    //lateinit var navController: NavHostController

    companion object{
        val TAG: String = MainActivity::class.java.simpleName
    }

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)
        setContent {
            EshopZTheme {
                //LoginScreen(navController = navController, auth)
                Navigation(auth)
            }
        }
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){

        }
    }
}

@Composable
fun Background(){
    Image(painter = painterResource(id = R.drawable.background),
        contentDescription = "Background Image",
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()

    )
}

@Composable
fun Homepage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(27.dp)
    ) {
        Text(text = "Welcome to EshopZ",
            color = Color.Red,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp,
            textAlign= TextAlign.Center
        )

        Spacer(modifier = Modifier.padding(bottom=27.dp))
        Button(onClick = {}) {
            Text("Get started now")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EshopZTheme {
        Background()
    }
}