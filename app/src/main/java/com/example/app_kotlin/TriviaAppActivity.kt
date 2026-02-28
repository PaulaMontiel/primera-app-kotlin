package com.example.app_kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app_kotlin.trivia.QuizUiState
import com.example.app_kotlin.trivia.QuizViewModel
import com.example.app_kotlin.ui.theme.AppkotlinTheme

class TriviaAppActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppkotlinTheme {

                val viewModel: QuizViewModel = viewModel()
                val state = viewModel.uiState.collectAsStateWithLifecycle().value

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Trivia App", color = Color.White) },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = "Volver",
                                        tint = Color.White
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFFF06292)
                            )
                        )
                    },
                ) { innerPadding ->

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {

                        if (state.isFinished) {

                            //  PANTALLA FINAL
                            FinishedScreen(
                                score = state.score,
                                total = state.questions.size * 100,
                                onRetry = viewModel::resetQuiz
                            )

                        } else {

                            //  PANTALLA DE PREGUNTAS
                            QuestionScreen(
                                state = state,
                                onSelectedOption = viewModel::onSelectedOption,
                                onConfirm = viewModel::onConfirmAnswer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionScreen(
    state: QuizUiState,
    onSelectedOption: (Int) -> Unit,
    onConfirm: () -> Unit,
) {

    val q = state.currentQuestion ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        //  VIDAS
        Text("Vidas: ❤️ ${state.lives}")

        // Progreso de preguntas
        Text(
            text = "Pregunta ${state.currentIndex + 1} de ${state.questions.size}",
            style = MaterialTheme.typography.titleMedium
        )

        // Pregunta
        Text(
            text = q.title,
            style = MaterialTheme.typography.headlineSmall
        )

        // Opciones
        q.options.forEachIndexed { index, option ->

            val isSelected = state.selectedIndex == index

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectedOption(index) },
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = if (isSelected) 12.dp else 1.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelectedOption(index) }
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(option)
                }
            }
        }

        // BOTÓN CONFIRMAR / VER RESULTADOS
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            enabled = state.selectedIndex != null
        ) {
            Text(
                if (state.isLastQuestion)
                    "Ver resultados"
                else
                    "Confirmar"
            )
        }

        // FEEDBACK INMEDIATO
        state.feedback?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleMedium
            )
        }

        // PORCENTAJE DE AVANCE
        Text(
            text = "Porcentaje avance: ${state.progressPercent}%"
        )
    }
}

@Composable
fun FinishedScreen(
    score: Int,
    total: Int,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "¡Quiz finalizado!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Tu puntaje: $score / $total",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(64.dp))

        Button(onClick = onRetry) {
            Text("Reintentar Quiz")
        }
    }
}