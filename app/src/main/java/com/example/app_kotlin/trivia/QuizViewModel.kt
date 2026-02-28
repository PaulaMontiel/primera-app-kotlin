package com.example.app_kotlin.trivia

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

    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun onSelectedOption(index: Int) {
        val current = _uiState.value
        if (current.isFinished) return

        _uiState.value = current.copy(selectedIndex = index)
    }

    fun onConfirmAnswer() {

        val current = _uiState.value
        val selected = current.selectedIndex ?: return
        val question = current.currentQuestion ?: return

        val isCorrect = selected == question.correctIndex

        val newScore = if (isCorrect) current.score + 100 else current.score
        val feedback = if (isCorrect) "✅ Correcto" else "❌ Incorrecto"

        // ⭐ Sistema de vidas
        val newLives = if (isCorrect) current.lives else current.lives - 1

        // ⭐ Sin vidas → terminar
        if (newLives <= 0) {
            _uiState.value = current.copy(
                score = newScore,
                feedback = feedback,
                lives = 0,
                isFinished = true
            )
            return
        }

        // ⭐ Avanzar pregunta
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

    fun resetQuiz() {
        _uiState.value = QuizUiState(
            questions = seedQuestions()
        )
    }

    private fun seedQuestions(): List<Question> {
        return listOf(
            Question(
                id = 1,
                title = "¿Que palabra clave se usa para declarar una variable inmutable en Kotlin?",
                options = listOf("var", "val", "let", "const"),
                correctIndex = 1
            ),
            // ... tus otras preguntas (déjalas igual)
        )
    }
}