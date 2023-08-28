package com.project.notebookapp.utils

import com.project.notebookapp.R

enum class NotePriority(val colorResId: Int) {
    LOW(R.color.red),
    MEDIUM(R.color.yellow),
    HIGH(R.color.green)
}