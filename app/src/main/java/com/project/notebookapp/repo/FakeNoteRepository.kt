package com.project.notebookapp.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.project.notebookapp.data.Note
import com.project.notebookapp.utils.NotePriority

class FakeNoteRepository : NoteRepository {
    private var saveNoteCalled: Boolean = false

    var savedNote: Note? = null
        private set

    private var updateNoteCalled: Boolean = false

    var updatedNote: Note? = null
        private set

    private var deleteNoteCalled: Boolean = false

    var deletedNote: Note? = null
        private set

    var getNoteByIdCalled: Boolean = false
        private set

    var noteIdToRetrieve: Long = 0
        private set

    var searchNotesCalled: Boolean = false
        private set

    var searchQuery: String = ""
        private set

    override val allNotes: LiveData<List<Note>> = MutableLiveData()
    fun setSampleNote() {
        (allNotes as MutableLiveData).value = listOf(
            Note(
                title = "Sample Title",
                content = "Sample Content",
                timestamp = System.currentTimeMillis(),
                priority = NotePriority.HIGH,
                color = 456
            )
        )
    }

    override suspend fun saveNote(note: Note) {
        saveNoteCalled = true
        savedNote = note
    }

    override suspend fun updateNote(note: Note) {
        updateNoteCalled = true
        updatedNote = note
    }

    override suspend fun deleteNote(note: Note) {
        deleteNoteCalled = true
        deletedNote = note
    }

    override fun getNoteById(noteId: Long): LiveData<Note> {
        getNoteByIdCalled = true
        noteIdToRetrieve = noteId
        return MutableLiveData()
    }

    override fun searchNotes(query: String): LiveData<List<Note>> {
        searchNotesCalled = true
        searchQuery = query
        return MutableLiveData()
    }

    override fun getNumberOfNotes(): Int {
        return 0
    }

}

