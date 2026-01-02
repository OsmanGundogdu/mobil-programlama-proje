package com.example.mobil_programlama_proje.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mobil_programlama_proje.database.dao.CategoryDao
import com.example.mobil_programlama_proje.database.dao.NoteDao
import com.example.mobil_programlama_proje.database.dao.UserDao
import com.example.mobil_programlama_proje.database.entity.Category
import com.example.mobil_programlama_proje.database.entity.User
import com.example.mobil_programlama_proje.model.Note

@Database(
    entities = [
        User::class,
        Note::class,
        Category::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_note_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        fun getInstance(context: Context): AppDatabase = getDatabase(context)
    }
}