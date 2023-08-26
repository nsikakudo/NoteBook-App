package com.project.notebookapp.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.project.notebookapp.R
import com.project.notebookapp.databinding.FragmentAboutUsBinding
import com.project.notebookapp.databinding.FragmentNoteBinding
import com.project.notebookapp.ui.adapter.NoteAdapter
import com.project.notebookapp.ui.viewmodel.NoteViewModel

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var rvAdapter: NoteAdapter

    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        rvAdapter = NoteAdapter { note ->
            viewModel.deleteNote(note)
        }
        binding.noteRecyclerView.adapter = rvAdapter
        viewModel.notes.observe(viewLifecycleOwner, Observer { notes ->
        rvAdapter.submitList(notes)
        })

        binding.btnAddNotes.setOnClickListener {
            findNavController().navigate(R.id.action_noteFragment_to_newNoteModal)
        }

        binding.btnInfo.setOnClickListener {
            showAboutDialog()
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.noteRecyclerView)

    }

    private val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            val noteToDelete = rvAdapter.getNoteAt(position)
            viewModel.deleteNote(noteToDelete)
        }
    }

    private fun showAboutDialog() {
        val binding = FragmentAboutUsBinding.inflate(LayoutInflater.from(requireContext()))
        val builder = AlertDialog.Builder(requireContext())
            .setView(binding.root)
        val dialog = builder.create()
        dialog.show()
    }

}