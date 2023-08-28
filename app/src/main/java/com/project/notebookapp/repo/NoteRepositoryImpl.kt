package com.project.notebookapp.repo

import android.util.Log
import com.project.notebookapp.data.Note
import com.project.notebookapp.utils.NotePriority

class NoteRepositoryImpl: NoteRepository {

    private val notes: MutableList<Note> = mutableListOf(

        Note("Techmeme",
                "Techmeme provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                System.currentTimeMillis(),
                NotePriority.HIGH
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                System.currentTimeMillis(),
                NotePriority.LOW
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                System.currentTimeMillis(),
                NotePriority.MEDIUM
            ),
            Note("Techmeme",
                "Techmeme provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                System.currentTimeMillis(),
                NotePriority.HIGH
            ),
            Note(
                "Ars Technica",
                "Ars Technica provides essential tech news of the moment." +
                        " It provides the top news and commentary for technology's leaders.",
                System.currentTimeMillis(),
                NotePriority.LOW
            )
    )
    override fun getAllNotes(): List<Note> {
        return notes
    }

    override fun addNote(note: Note) {
        notes.add(note)
        Log.d("NoteRepositoryImpl", "Note added: $note")
    }

    override fun deleteNote(note: Note) {
        notes.remove(note)
        Log.d("NoteRepositoryImpl", "Note deleted: $note")
    }


}