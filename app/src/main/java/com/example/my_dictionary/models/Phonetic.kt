package com.example.my_dictionary.models

data class Phonetic(
    val audio: String,
    val license: LicenseX,
    val sourceUrl: String,
    val text: String
)