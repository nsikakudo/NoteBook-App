package com.project.notebookapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.notebookapp.utils.NotePriority

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val timestamp: Long,
    val priority: NotePriority
)

