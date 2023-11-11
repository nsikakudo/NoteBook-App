package com.project.notebookapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.notebookapp.utils.NotePriority

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var title: String,
    var content: String,
    var timestamp: Long,
    var priority: NotePriority,
    var color: Int
)

