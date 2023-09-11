package com.project.notebookapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.notebookapp.data.Note
import com.project.notebookapp.repo.NoteRepository
import kotlinx.coroutines.launch


class NoteViewModel(
    private val noteRepository: NoteRepository
): ViewModel() {

    val allNotes: LiveData<List<Note>> = noteRepository.allNotes

    fun insertOrUpdate(note: Note) {
        viewModelScope.launch {
            noteRepository.insertOrUpdate(note)
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            noteRepository.delete(note)
        }
    }

    fun getNoteById(noteId: Long): LiveData<Note> {
        return noteRepository.getNoteById(noteId)
    }

}