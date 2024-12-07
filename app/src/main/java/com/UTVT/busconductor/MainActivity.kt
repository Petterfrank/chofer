package com.UTVT.busconductor

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.UTVT.busconductor.ui.theme.BusConductorTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //FirebaseApp.initializeApp(this)
        setContent {
            BusConductorTheme {
                MainScreen(
                    onStartEmisor = { navigateToEmisor() },
                    onStartReceptor = { navigateToReceptor() }
                )
            }
        }
    }

    private fun navigateToEmisor() {
        val intent = Intent(this, EmisorActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToReceptor() {
        val intent = Intent(this, ReceptorActivity::class.java)
        startActivity(intent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onStartEmisor: () -> Unit, onStartReceptor: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bus Conductor") }
            )
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = onStartEmisor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Text("Iniciar como Emisor")
                }
                Button(
                    onClick = onStartReceptor,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar como Receptor")
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    BusConductorTheme {
        MainScreen({}, {})
    }
}
