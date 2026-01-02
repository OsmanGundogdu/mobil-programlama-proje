package com.example.mobil_programlama_proje.data

import com.example.mobil_programlama_proje.database.dao.NoteDao
import com.example.mobil_programlama_proje.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {

    // MAILE GÖRE GETİR
    fun getNotesByUser(email: String): Flow<List<Note>> {
        return noteDao.getNotesByUser(email)
    }

    suspend fun getNoteById(id: String): Note? {
        return noteDao.getNoteById(id)
    }

    suspend fun insertNote(note: Note) {
        noteDao.insert(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.update(note)
    }

    suspend fun deleteNote(note: Note) {
        noteDao.delete(note)
    }
}