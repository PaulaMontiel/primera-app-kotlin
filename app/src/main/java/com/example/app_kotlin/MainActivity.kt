package com.example.app_kotlin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.app_kotlin.ui.theme.AppkotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppkotlinTheme {
                Scaffold { innerPadding ->
                    MainMenu(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainMenu(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        // Imagen de fondo de la pantalla
        Image(
            painter = painterResource(R.drawable.pastel),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Mis Mini Proyectos",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Counter App
            MenuButton(
                text = "Counter App",
                imageRes = R.drawable.glitter
            ) {
                val intent = Intent(context, CounterAppActivity::class.java)
                context.startActivity(intent)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Todo App
            MenuButton(
                text = "Todo App",
                imageRes = R.drawable.sparkle

            ) {
                val intent = Intent(context, TodoAppActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}