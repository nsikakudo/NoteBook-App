package com.project.notebookapp.data

import com.project.notebookapp.utils.NotePriority

data class Note(
    val title: String,
    val content: String,
    val priority: NotePriority
)

