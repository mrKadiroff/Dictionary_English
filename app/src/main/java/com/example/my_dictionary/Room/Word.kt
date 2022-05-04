package com.example.my_dictionary.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Word {

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    @ColumnInfo(name = "words")
    var word: String? = null

    @ColumnInfo(name = "trans")
    var transcription: String? = null

    @ColumnInfo(name = "savedd")
    var saved: Boolean? = null

    @ColumnInfo(name = "link")
    var uri: String? = null

}