package com.project.notebookapp.repo

import com.project.notebookapp.data.Note

interface NoteRepository {

    fun getAllNotes(): List<Note>
    fun addNote(note: Note)
    fun deleteNote(note: Note)
}