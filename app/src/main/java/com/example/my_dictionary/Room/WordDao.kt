package com.example.my_dictionary.Room

import androidx.room.*

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWord(word: Word)

    @Update
    fun updateTalaba(word: Word)

    @Query("select * from word")
    fun getAllWords(): List<Word>


    @Query("select * from word where savedd=:status")
    fun getWordByStatus(status:Boolean) : List<Word>


    @Query("select * from word where words=:name")
    fun getWordByName(name:String) : Word
}