package com.project.notebookapp.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.project.notebookapp.R
import com.project.notebookapp.data.Note
import com.project.notebookapp.databinding.FragmentAboutUsBinding
import com.project.notebookapp.databinding.FragmentNoteBinding
import com.project.notebookapp.repo.NoteRepository
import com.project.notebookapp.ui.adapter.NoteAdapter
import com.project.notebookapp.ui.adapter.NoteClickListener
import com.project.notebookapp.ui.viewmodel.NoteViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val noteRepository: NoteRepository by inject()

    private val viewModel: NoteViewModel by viewModel { parametersOf(noteRepository)  }

    private val rvAdapter: NoteAdapter by lazy {
        NoteAdapter(object : NoteClickListener {
            override fun editNote(note: Note) {
                findNavController().navigate(NoteFragmentDirections.actionNoteFragmentToNewNoteModal())
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            rvAdapter.submitList(notes)
        }

        binding.noteRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rvAdapter
        }

        binding.btnAddNotes.setOnClickListener {
            findNavController().navigate(R.id.action_noteFragment_to_newNoteModal)
        }

        binding.btnInfo.setOnClickListener {
            showAboutDialog()
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = rvAdapter.currentList[position]
                viewModel.deleteNote(note)
                Snackbar.make(binding.root, "Note deleted successfully", Snackbar.LENGTH_LONG)
                    .apply {
                        setAction("Undo") {
                            viewModel.addNote(note)
                        }
                            .show()
                    }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.noteRecyclerView)
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