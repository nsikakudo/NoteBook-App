package com.project.notebookapp.utils

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    if (visibility == View.VISIBLE) {
        visibility = View.GONE
    }
}