package com.example.offlinetodos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [TodoEntity::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        fun getDatabase(context: Context): TodoDatabase =
            Room.databaseBuilder(context,
                TodoDatabase::class.java,
                "todo_db").build()
    }
}
