package com.project.notebookapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.project.notebookapp.R
import com.project.notebookapp.databinding.FragmentNewNoteModalBinding

class NewNoteFragment : Fragment() {

    private var _binding: FragmentNewNoteModalBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewNoteModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.fabAddColour.setOnClickListener {
            Toast.makeText(requireContext(), "select color", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_newNoteModal_to_colourModalSheet)
        }

        binding.topAppBar.setNavigationOnClickListener {
            Toast.makeText(requireContext(), "Return to Note Fragment", Toast.LENGTH_SHORT).show()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.save -> {
                    Toast.makeText(requireContext(), "New Note Save", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.share -> {
                    Toast.makeText(requireContext(), "Note Shared", Toast.LENGTH_SHORT).show()
                    true
                }
                else ->{
                    false
                }
            }

        }

    }

}