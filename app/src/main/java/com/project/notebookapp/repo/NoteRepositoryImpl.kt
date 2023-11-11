package com.project.notebookapp.repo

import androidx.lifecycle.LiveData
import com.project.notebookapp.data.Note
import com.project.notebookapp.data.NoteDao

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {

    override val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    override suspend fun saveNote(note: Note) {
        noteDao.addNote(note)
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override fun getNoteById(noteId: Long): LiveData<Note> {
        return noteDao.getNoteById(noteId)
    }

    override fun searchNotes(query: String): LiveData<List<Note>> {
        return noteDao.searchNotes("%$query%") // Adding '%' to search for partial matches
    }
    override fun getNumberOfNotes(): Int {
        return noteDao.getCount()
    }

}

