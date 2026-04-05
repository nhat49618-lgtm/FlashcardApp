package com.example.flashcardapp.data.repository

import com.example.flashcardapp.data.local.Flashcard
import com.example.flashcardapp.data.local.FlashcardDao
import javax.inject.Inject

class FlashcardRepository @Inject constructor(
    private val dao: FlashcardDao
) {

    fun getAll() = dao.getAll()

    suspend fun insert(card: Flashcard) {
        dao.insert(card)
    }
}