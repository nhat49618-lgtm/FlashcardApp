package com.example.flashcard.data

import retrofit2.http.GET// test commit

interface FlashcardApi {

    @GET("api.php?amount=10")
    suspend fun getFlashcards(): ApiResponse
}