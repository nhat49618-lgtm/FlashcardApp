package com.example.flashcardapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flashcardapp.ui.viewmodel.FlashcardViewModel

@Composable
fun FlashcardScreen(viewModel: FlashcardViewModel) {

    val list by viewModel.flashcards.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Button(onClick = {
            viewModel.addFlashcard("Hello", "Xin chào")
        }) {
            Text("Add Flashcard")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(list) { card ->
                Text("${card.question} - ${card.answer}")
            }
        }
    }
}