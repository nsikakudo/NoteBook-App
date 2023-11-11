package com.project.notebookapp.ui.adapter

import com.project.notebookapp.data.Note
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.notebookapp.R
import com.project.notebookapp.databinding.SearchNotesViewHolderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NoteAdapter(private val listener: NoteClickListener) :
    ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffUtil) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
            return NoteViewHolder(
                SearchNotesViewHolderBinding.bind(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.search_notes_view_holder, parent, false
                    )
                )
            )
        }

        override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
            val note = getItem(position)
            holder.bind(note)
        }

    inner class NoteViewHolder(private val binding: SearchNotesViewHolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: Note) = with(binding) {
            tvTopic.text = note.title
            tvContent.text = note.content

            val formattedTime = formatDate(note.timestamp)
            tvDate.text = formattedTime

            binding.cardView.setBackgroundColor(note.color)

            val priority = note.priority
            priorityLevel.text = priority.toString()
            priorityLevel.setTextColor(ContextCompat.getColor(itemView.context, priority.colorResId))

            cardView.setOnClickListener {
                listener.editNote(note)
            }
        }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy h:mm a", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }

        companion object {
            private val NoteDiffUtil = object : DiffUtil.ItemCallback<Note>() {
                override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                    return oldItem.timestamp == newItem.timestamp
                }

                override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }