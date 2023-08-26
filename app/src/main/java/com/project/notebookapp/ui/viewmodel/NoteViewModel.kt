package com.project.notebookapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.notebookapp.data.Note
import com.project.notebookapp.utils.NotePriority
import java.time.LocalDateTime

class NoteViewModel: ViewModel() {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: MutableLiveData<List<Note>> = _notes

    private val noteList = mutableListOf<Note>()
//    val addNotes: List<Note> get() = noteList

    init {
        loadNotes()
    }

    fun addNote(note: Note) {
        noteList.add(note)
    }

    private fun loadNotes(){
        _notes.value = listOf(
            Note("Techmeme",
                "Techmeme provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                timestamp = LocalDateTime.now(),
                NotePriority.HIGH
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                timestamp = LocalDateTime.now(),
                NotePriority.LOW
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                timestamp = LocalDateTime.now(),
                NotePriority.MEDIUM
            ),
            Note("Techmeme",
                "Techmeme provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                timestamp = LocalDateTime.now(),
                NotePriority.HIGH
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                timestamp = LocalDateTime.now(),
                NotePriority.LOW
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                timestamp = LocalDateTime.now(),
                NotePriority.MEDIUM
            ),
            Note("Techmeme",
                "Techmeme provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                timestamp = LocalDateTime.now(),
                NotePriority.HIGH
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                timestamp = LocalDateTime.now(),
                NotePriority.LOW
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                timestamp = LocalDateTime.now(),
                NotePriority.MEDIUM
            ),
        )
    }

//    fun addNote(note: Note) {
//        _notes.value?.add(note)
//        _notes.value = _notes.value
//    }
}