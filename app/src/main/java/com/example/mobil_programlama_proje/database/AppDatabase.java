package com.example.mobil_programlama_proje.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobil_programlama_proje.database.dao.NoteDao;
import com.example.mobil_programlama_proje.database.entity.Category;
import com.example.mobil_programlama_proje.database.entity.Note;
import com.example.mobil_programlama_proje.database.entity.User;

@Database(
        entities = {
                User.class,
                Note.class,
                Category.class
        },
        version = 1
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract NoteDao noteDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "smart_note_db"
            ).allowMainThreadQueries().build();
        }
        return instance;
    }
}
