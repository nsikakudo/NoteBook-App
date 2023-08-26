package com.project.notebookapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.notebookapp.R
import com.project.notebookapp.data.Note
import com.project.notebookapp.databinding.FragmentColorPaletteModalBinding
import com.project.notebookapp.databinding.FragmentNewNoteModalBinding
import com.project.notebookapp.databinding.PriorityLevelModalBinding
import com.project.notebookapp.ui.viewmodel.NoteViewModel
import com.project.notebookapp.utils.NotePriority
import java.time.LocalDateTime

class NewNoteFragment : Fragment() {

    private var _binding: FragmentNewNoteModalBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NoteViewModel

    private var isExpanded = false

    private val fromBottomFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.from_bottom_fab)
    }

    private val toBottomFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.to_bottom_fab)
    }

    private val rotateClockWiseFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_clock_wise)
    }

    private val rotateAntiClockWiseFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_anti_clock_wise)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewNoteModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[NoteViewModel::class.java]

//        binding.saveButton.setOnClickListener {
//            val title = binding.edtTitle.text.toString()
//            val content = binding.edtNote.text.toString()
//            val priority = when (binding.radioGroupPriority.checkedRadioButtonId) {
//                R.id.radioHigh -> NotePriority.HIGH
//                R.id.radioMedium -> NotePriority.MEDIUM
//                R.id.radioLow -> NotePriority.LOW
//                else -> NotePriority.MEDIUM
//            }
//
//            if (title.isNotEmpty() && content.isNotEmpty()) {
//                val newNote = Note(title, content, priority)
//                viewModel.addNote(newNote)
//                requireActivity().supportFragmentManager.popBackStack()
//            }
//        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val content = binding.edtNote.text.toString()
            val timestamp = LocalDateTime.now()
            val priority = NotePriority.MEDIUM

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val newNote = Note(title, content, timestamp, priority)
                viewModel.addNote(newNote)
                requireActivity().supportFragmentManager.popBackStack()
//                findNavController().navigateUp()
            }
            Toast.makeText(requireContext(), "Note Saved", Toast.LENGTH_SHORT).show()
        }

        binding.shareButton.setOnClickListener {
            Toast.makeText(requireContext(), "Note Shared", Toast.LENGTH_SHORT).show()
        }

        binding.addColor!!.setOnClickListener {
            showColorPalette()
        }

        binding.addPriorityLevel!!.setOnClickListener {
            showPriorityLevel()
        }

        binding.addNoteFeatures.setOnClickListener {
            if (isExpanded){
                shrinkFab()
            }else{
                expandFab()
            }
        }
    }

    private fun shrinkFab(){
        binding.transparentBg!!.visibility = View.GONE
        binding.addNoteFeatures.startAnimation(rotateAntiClockWiseFabAnim)
        binding.addPriorityLevel?.startAnimation(toBottomFabAnim)
        binding.addColor?.startAnimation(toBottomFabAnim)
        binding.tvColor?.startAnimation(toBottomFabAnim)
        binding.tvPriority?.startAnimation(toBottomFabAnim)

        isExpanded = !isExpanded
    }

    private fun expandFab(){
        binding.transparentBg!!.visibility = View.VISIBLE
        binding.addNoteFeatures.startAnimation(rotateClockWiseFabAnim)
        binding.addPriorityLevel?.startAnimation(fromBottomFabAnim)
        binding.addColor?.startAnimation(fromBottomFabAnim)
        binding.tvColor?.startAnimation(fromBottomFabAnim)
        binding.tvPriority?.startAnimation(fromBottomFabAnim)

        isExpanded = !isExpanded
    }

    private fun showPriorityLevel(){
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val binding = PriorityLevelModalBinding.inflate(LayoutInflater.from(requireContext()))
        val bottomSheetView = binding.root
        binding.btnCancel!!.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun showColorPalette() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val binding = FragmentColorPaletteModalBinding.inflate(LayoutInflater.from(requireContext()))
        val bottomSheetView = binding.root
        binding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

}