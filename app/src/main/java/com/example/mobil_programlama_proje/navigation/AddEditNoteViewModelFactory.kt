package com.example.mobil_programlama_proje.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobil_programlama_proje.data.NoteRepository
import com.example.mobil_programlama_proje.viewmodel.AddEditNoteViewModel

/**
 * Factory for creating AddEditNoteViewModel instances with dependencies.
 * 
 * @param repository The note repository for data access
 */
class AddEditNoteViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditNoteViewModel::class.java)) {
            return AddEditNoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
