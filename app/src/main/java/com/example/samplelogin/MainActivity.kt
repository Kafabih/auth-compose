package com.example.samplelogin

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.auth0.android.Auth0
import com.example.samplelogin.ui.component.CustomButton
import com.example.samplelogin.ui.theme.ColorScreenBackground
import com.example.samplelogin.ui.theme.Purple40
import com.example.samplelogin.ui.theme.SampleLoginTheme
import com.example.samplelogin.viewmodel.MainActivityViewModel

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SampleLoginTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(mainViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(viewModel: MainActivityViewModel) {

    lateinit var account: Auth0
    val mContext = LocalContext.current

    var accountName by rememberSaveable { mutableStateOf("") }
    var accountEmail by rememberSaveable { mutableStateOf("") }


    SideEffect {
        account = Auth0(
            viewModel.clientID,
            viewModel.domain
        )
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorScreenBackground)
    ) {
        Column {
            Box {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Sample Login",
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

            CustomButton(Purple40, text = if(viewModel.userIsAuthenticated) "Authenticated" else "Authenticate") {
                viewModel.loginWithBrowser(mContext, account)
            }

//            Spacer(modifier = Modifier.weight(1f))

            CustomButton(color = Color.Gray, text = "Logout") {
                viewModel.logout(mContext, account)
            }

            if(viewModel.userIsAuthenticated){
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Name: ${viewModel.nameData ?: ""}\n" +
                                "Email: ${viewModel.emailData ?: ""}",
                        color = Color.Black,
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                }
            }

        }

    }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SampleLoginTheme {
        Greeting(MainActivityViewModel())
    }
}