package com.example.flashcardapp.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Flashcard)

    @Query("SELECT * FROM Flashcard")
    fun getAll(): Flow<List<Flashcard>>
}