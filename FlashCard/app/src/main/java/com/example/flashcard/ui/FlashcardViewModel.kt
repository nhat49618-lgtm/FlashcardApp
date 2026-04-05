package com.example.flashcard.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.flashcard.data.AppDatabase
import com.example.flashcard.data.FlashcardRepository
import com.example.flashcard.data.RetrofitInstance
import com.example.flashcard.model.Flashcard
import com.example.flashcard.util.TtsManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlashcardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardRepository
    val allFlashcards: StateFlow<List<Flashcard>>
    val cardsToReview: StateFlow<List<Flashcard>>
    private val ttsManager: TtsManager = TtsManager(application)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = FlashcardRepository(database.flashcardDao())

        allFlashcards = repository.allFlashcards.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        cardsToReview = repository.getFlashcardsToReview().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // ✅ THÊM FLASHCARD THỦ CÔNG
    fun addFlashcard(front: String, back: String) {
        viewModelScope.launch {
            repository.addFlashcard(front, back)
        }
    }

    // ✅ UPDATE SAU KHI REVIEW
    fun updateAfterReview(flashcard: Flashcard, quality: Int) {
        viewModelScope.launch {
            repository.updateFlashcardAfterReview(flashcard, quality)
        }
    }

    // ✅ 🔥 THÊM HÀM GỌI API
    fun fetchFlashcardsFromApi() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getFlashcards()

                val flashcards = response.results.map {
                    Flashcard(
                        front = it.question,
                        back = it.correct_answer
                    )
                }

                // 👉 Lưu vào database
                flashcards.forEach {
                    repository.insert(it)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // ✅ TEXT TO SPEECH
    fun speak(text: String) {
        ttsManager.speak(text)
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}