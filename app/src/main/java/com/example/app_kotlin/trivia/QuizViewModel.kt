package com.example.app_kotlin.trivia

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuizViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        QuizUiState(
            questions = seedQuestions()
        )
    )

    /**
     * "Solo lectura"
     * UI -->
     * uiState.value = ... ABC
     */
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun onSelectedOption(index: Int) {
        // Obtener el estado actual
        val current = _uiState.value

        // Si el usuario termino la trivia
        if (current.isFinished) return

        // actualizar el estado
        _uiState.value = current.copy(selectedIndex = index)
    }

    fun onConfirmAnswer() {

        val current = _uiState.value

        val selected = current.selectedIndex ?: return
        val question = current.currentQuestion ?: return

        val isCorrect = selected == question.correctIndex

        val newScore = if (isCorrect) current.score + 100 else current.score
        val feedback = if (isCorrect) "✅ Correcto" else "❌ Incorrecto"

        // SISTEMA DE VIDAS
        val newLives = if (isCorrect) current.lives else current.lives - 1

        // Sin vidas → finalizar automáticamente
        if (newLives <= 0) {
            _uiState.value = current.copy(
                score = newScore,
                feedback = feedback,
                lives = 0,
                isFinished = true
            )
            return
        }

        //  Avanzar o finalizar si es última
        val nextIndex = current.currentIndex + 1
        val finished = nextIndex >= current.questions.size

        _uiState.value = current.copy(
            score = newScore,
            feedback = feedback,
            lives = newLives,
            currentIndex = nextIndex,
            selectedIndex = null,
            isFinished = finished
        )
    }


    private fun seedQuestions() : List<Question> {
        return listOf(
            Question(
                id = 1,
                title = "¿Que palabra clave se usa para declarar una variable inmutable en Kotlin?",
                options = listOf("var", "val", "let", "const"),
                correctIndex = 1
            ),
            Question(
                id = 2,
                title = "En Jetpack Compose, ¿que anotacion marca una funcion como UI?",
                options = listOf("@UI", "@Widget", "@Composable", "@Compose"),
                correctIndex = 2
            ),
            Question(
                id = 3,
                title = "¿Que componente se usa para listas eficientes y scrolleables?",
                options = listOf("Column", "RecyclerView", "Stack", "LazyColumn"),
                correctIndex = 3
            ),
            Question(
                id = 4,
                title = "La instrucción que permite restaurar estado tras recreación de Activity es",
                options = listOf("intentData", "savedInstanceState", "activityState", "bundleConfig"),
                correctIndex = 1
            ),

            // NUEVAS PREGUNTAS

            Question(
                id = 5,
                title = "¿Que componente administra datos relacionados a la UI respetando el ciclo de vida?",
                options = listOf("Activity", "Fragment", "ViewModel", "Service"),
                correctIndex = 2
            ),
            Question(
                id = 6,
                title = "¿Que corrutina se usa para tareas en el hilo principal?",
                options = listOf("Dispatchers.IO", "Dispatchers.Default", "Dispatchers.Main", "Dispatchers.Global"),
                correctIndex = 2
            ),
            Question(
                id = 7,
                title = "¿Que funcion se usa para iniciar una corrutina en Compose?",
                options = listOf("launchEffect", "rememberCoroutine", "LaunchedEffect", "startCoroutine"),
                correctIndex = 2
            ),
            Question(
                id = 8,
                title = "¿Que componente permite navegación entre pantallas en Compose?",
                options = listOf("NavHost", "NavigatorLayout", "RouteManager", "ScreenHost"),
                correctIndex = 0
            ),
            Question(
                id = 9,
                title = "¿Que layout permite superponer elementos en Compose?",
                options = listOf("Column", "Row", "Box", "StackLayout"),
                correctIndex = 2
            ),
            Question(
                id = 10,
                title = "¿Que función convierte un Flow en estado observable en Compose?",
                options = listOf("collect()", "observe()", "collectAsState()", "asLiveData()"),
                correctIndex = 2
            ),
            Question(
                id = 11,
                title = "¿Que componente tradicional se reemplaza por LazyColumn en Compose?",
                options = listOf("ScrollView", "ListView", "RecyclerView", "GridView"),
                correctIndex = 2
            ),
            Question(
                id = 12,
                title = "¿Que palabra clave permite manejar valores nulos en Kotlin?",
                options = listOf("nullable", "nullsafe", "?", "!!"),
                correctIndex = 2
            ),
            Question(
                id = 13,
                title = "¿Que operador se usa para acceso seguro a propiedades nulas?",
                options = listOf(".", "?.", "!!", "::"),
                correctIndex = 1
            ),
            Question(
                id = 14,
                title = "¿Que componente se usa para ejecutar tareas en segundo plano de forma programada?",
                options = listOf("WorkerManager", "WorkManager", "JobService", "TaskRunner"),
                correctIndex = 1
            ),
            Question(
                id = 15,
                title = "¿Que arquitectura recomienda Google para apps Android modernas?",
                options = listOf("MVC", "MVP", "MVVM", "CleanOnly"),
                correctIndex = 2
            )
        )
    }

}