package com.project.notebookapp.data

import com.project.notebookapp.utils.NotePriority
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Note(
    val title: String,
    val content: String,
    val timestamp: LocalDateTime,
    val priority: NotePriority
){
    val timestampString: String
        get() = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}

