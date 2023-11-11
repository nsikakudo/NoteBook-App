package com.project.notebookapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.notebookapp.data.Note
import com.project.notebookapp.repo.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class NoteViewModel(
    private val noteRepository: NoteRepository,
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
): ViewModel() {

    val allNotes: LiveData<List<Note>> = noteRepository.allNotes

    fun saveNote(note: Note) {
        viewModelScope.launch(ioDispatcher) {
            noteRepository.saveNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch(ioDispatcher) {
            noteRepository.updateNote(note)
        }
    }

    fun searchNotes(query: String): LiveData<List<Note>> {
        return noteRepository.searchNotes(query)
    }
    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(note)
        }
    }
    fun getNoteById(noteId: Long): LiveData<Note> {
        return noteRepository.getNoteById(noteId)
    }

}