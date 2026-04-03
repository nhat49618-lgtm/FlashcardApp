package com.example.baiktragk

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

data class Subject(
    val name: String,
    val credits: Int,
    val isTheory: Boolean
)

class MainViewModel : ViewModel() {

    var subjects = mutableStateListOf<Subject>()
        private set

    fun addSubject(subject: Subject) {
        subjects.add(subject)
    }

    fun removeSubject(subject: Subject) {
        subjects.remove(subject)
    }

    fun getTheoryCredits(): Int {
        return subjects.filter { it.isTheory }.sumOf { it.credits }
    }

    fun getPracticeCredits(): Int {
        return subjects.filter { !it.isTheory }.sumOf { it.credits }
    }
}