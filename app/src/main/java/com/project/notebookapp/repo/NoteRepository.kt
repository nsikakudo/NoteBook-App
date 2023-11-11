package com.project.notebookapp.repo

import androidx.lifecycle.LiveData
import com.project.notebookapp.data.Note

interface NoteRepository {

    val allNotes: LiveData<List<Note>>
    suspend fun saveNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNoteById(noteId: Long): LiveData<Note>
    fun searchNotes(query: String): LiveData<List<Note>>
    fun getNumberOfNotes(): Int
}
