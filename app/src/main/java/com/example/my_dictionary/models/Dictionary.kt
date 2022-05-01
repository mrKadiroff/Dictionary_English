package com.example.my_dictionary.models

data class Dictionary(
    val license: License,
    val meanings: List<Meaning>,
    val phonetics: List<Phonetic>,
    val sourceUrls: List<String>,
    val word: String
)