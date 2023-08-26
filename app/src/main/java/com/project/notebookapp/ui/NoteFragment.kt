package com.project.notebookapp.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.project.notebookapp.R
import com.project.notebookapp.databinding.FragmentAboutUsBinding
import com.project.notebookapp.databinding.FragmentNoteBinding
import com.project.notebookapp.ui.adapter.NoteAdapter
import com.project.notebookapp.ui.viewmodel.NoteViewModel

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

//    private lateinit var rvAdapter: NoteAdapter
//
//    private lateinit var viewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = NoteViewModel()
        val adapter = NoteAdapter()
        binding.noteRecyclerView.adapter = adapter
        viewModel.notes.observe(viewLifecycleOwner, Observer { notes ->
        adapter.submitList(notes)
        })

        binding.btnAddNotes.setOnClickListener {
            findNavController().navigate(R.id.action_noteFragment_to_newNoteModal)
        }

        binding.btnInfo.setOnClickListener {
            showAboutDialog()
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