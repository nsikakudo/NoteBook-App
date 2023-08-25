package com.project.notebookapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.notebookapp.data.Note
import com.project.notebookapp.utils.NotePriority

class NoteViewModel: ViewModel() {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: MutableLiveData<List<Note>> = _notes

    init {
        loadNotes()
    }

    private fun loadNotes(){
        _notes.value = listOf(
            Note("Techmeme",
                "Techmeme provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                NotePriority.HIGH
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                NotePriority.LOW
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                NotePriority.MEDIUM
            ),
            Note("Techmeme",
                "Techmeme provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                NotePriority.HIGH
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                NotePriority.LOW
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                NotePriority.MEDIUM
            ),
            Note("Techmeme",
                "Techmeme provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                NotePriority.HIGH
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                NotePriority.LOW
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                NotePriority.MEDIUM
            ),
        )
    }

//    fun addNote(note: Note) {
//        _notes.value?.add(note)
//        _notes.value = _notes.value
//    }
}