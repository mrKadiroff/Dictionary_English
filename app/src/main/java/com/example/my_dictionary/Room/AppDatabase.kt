package com.example.my_dictionary.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Saved::class],version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun saveDao(): SaveDao
    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (instance == null){
                instance = Room.databaseBuilder(context,AppDatabase::class.java,"data.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}