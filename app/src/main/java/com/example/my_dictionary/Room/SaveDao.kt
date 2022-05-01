package com.example.my_dictionary.Room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SaveDao {

    @Insert
    fun insertData(saved: Saved)

    @Query("select * from saved")
    fun getAllData(): List<Saved>
}