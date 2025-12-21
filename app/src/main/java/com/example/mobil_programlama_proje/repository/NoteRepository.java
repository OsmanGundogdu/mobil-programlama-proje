package com.example.mobil_programlama_proje.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.example.mobil_programlama_proje.data.local.AppDatabase;
import com.example.mobil_programlama_proje.data.local.NoteDao;
import com.example.mobil_programlama_proje.data.local.NoteEntity;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<NoteEntity>> allNotes;
    private ExecutorService executorService;

    public NoteRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        noteDao = database.noteDao();
        allNotes = noteDao.getAllNotes();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<NoteEntity>> getAllNotes() {
        return allNotes;
    }

    public void insert(NoteEntity note) {
        executorService.execute(() -> noteDao.insert(note));
    }

    public void update(NoteEntity note) {
        executorService.execute(() -> noteDao.update(note));
    }

    public void delete(NoteEntity note) {
        executorService.execute(() -> noteDao.delete(note));
    }

    public void deleteById(int noteId) {
        executorService.execute(() -> noteDao.deleteById(noteId));
    }

    public LiveData<List<NoteEntity>> searchNotes(String query) {
        return noteDao.searchNotes(query);
    }
}