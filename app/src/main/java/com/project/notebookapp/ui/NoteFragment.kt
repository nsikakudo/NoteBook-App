package com.project.notebookapp.ui

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.project.notebookapp.R
import com.project.notebookapp.databinding.FragmentAboutUsBinding
import com.project.notebookapp.databinding.FragmentNoteBinding

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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