package com.example.mobil_programlama_proje.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobil_programlama_proje.data.NoteRepository
import com.example.mobil_programlama_proje.viewmodel.NoteListViewModel

/**
 * Factory for creating NoteListViewModel with repository dependency.
 */
class NoteListViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteListViewModel::class.java)) {
            return NoteListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
