package com.project.notebookapp.repo

import androidx.lifecycle.LiveData
import com.project.notebookapp.data.Note

interface NoteRepository {

    val allNotes: LiveData<List<Note>>
    suspend fun insertOrUpdate(note: Note)
    suspend fun delete(note: Note)
    fun getNoteById(noteId: Long): LiveData<Note>
    fun getNumberOfNotes(): Int
}
