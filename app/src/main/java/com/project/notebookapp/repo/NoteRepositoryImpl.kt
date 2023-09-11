package com.project.notebookapp.repo

import androidx.lifecycle.LiveData
import com.project.notebookapp.data.Note
import com.project.notebookapp.data.NoteDao

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {

    override val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    override suspend fun insertOrUpdate(note: Note) {
        noteDao.insertOrUpdate(note)
    }

    override suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    override fun getNoteById(noteId: Long): LiveData<Note> {
        return noteDao.getNoteById(noteId)
    }
    override fun getNumberOfNotes(): Int {
        return noteDao.getCount()
    }

}

