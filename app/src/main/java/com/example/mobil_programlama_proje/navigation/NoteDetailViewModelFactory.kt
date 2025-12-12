package com.example.mobil_programlama_proje.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mobil_programlama_proje.data.NoteRepository
import com.example.mobil_programlama_proje.viewmodel.NoteDetailViewModel

/**
 * Factory for creating NoteDetailViewModel with repository dependency.
 */
class NoteDetailViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteDetailViewModel::class.java)) {
            return NoteDetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
