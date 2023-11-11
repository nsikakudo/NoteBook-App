package com.project.notebookapp.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.project.notebookapp.R
import com.project.notebookapp.data.Note
import com.project.notebookapp.databinding.FragmentColorPaletteModalBinding
import com.project.notebookapp.databinding.FragmentNewNoteModalBinding
import com.project.notebookapp.databinding.PriorityLevelModalBinding
import com.project.notebookapp.repo.NoteRepository
import com.project.notebookapp.ui.viewmodel.NoteViewModel
import com.project.notebookapp.utils.NotePriority
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
class NewNoteFragment : Fragment() {

    private var _binding: FragmentNewNoteModalBinding? = null
    private val binding get() = _binding!!

    private val noteRepository: NoteRepository by inject()

    private val viewModel: NoteViewModel by viewModel { parametersOf(noteRepository) }

    private var isExpanded = false

    private var selectedColor: Int = 0

    private var lastEditedTimestamp: Long? = null

    private var selectedPriority: NotePriority = NotePriority.HIGH

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

    private var existingNote: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewNoteModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val noteId = arguments?.getLong("noteId", -1L) ?: -1L

        val pattern = "dd-MM-yyyy h:mm a"
        val dateFormat = SimpleDateFormat(pattern)

        if (noteId != -1L) {
            viewModel.getNoteById(noteId).observe(viewLifecycleOwner) { note ->
                note?.let {
                    existingNote = it
                    binding.edtTitle.setText(it.title)
                    binding.edtNote.setText(it.content)
                    selectedPriority = it.priority
                    selectedColor = it.color
                    val formattedDate = dateFormat.format(Date(it.timestamp))
                    binding.editModeText.text = getString(R.string.edited_on, formattedDate)
                }
            }
        } else {
            existingNote = null
            binding.editModeText.visibility = View.INVISIBLE
        }

        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.saveButton.setOnClickListener {
            onSaveButtonClick()
        }

        binding.shareButton.setOnClickListener {
            shareNote()
        }

        binding.addColor.setOnClickListener {
            showColorPalette()
        }

        binding.addPriorityLevel.setOnClickListener {
            showPriorityLevel()
        }

        binding.addNoteFeatures.setOnClickListener {
            if (isExpanded){
                shrinkFab()
            }else{
                expandFab()
            }
        }

        binding.transparentBg.setOnClickListener {
            if (isExpanded) {
                shrinkFab()
            }
        }

    }

    private fun onSaveButtonClick() {
        val title = binding.edtTitle.text.toString()
        val content = binding.edtNote.text.toString()
        val timestamp = System.currentTimeMillis()

        if (title.isNotEmpty() && content.isNotEmpty()) {

            if (existingNote != null) {
                val updatedNote = existingNote!!.copy(
                    title = title,
                    content = content,
                    timestamp = timestamp,
                    priority = selectedPriority,
                    color = selectedColor
                )
                viewModel.updateNote(updatedNote)
            } else {
                val newNote = Note(
                    title = title,
                    content = content,
                    timestamp = System.currentTimeMillis(),
                    priority = selectedPriority,
                    color = selectedColor
                )
                viewModel.saveNote(newNote)
                showNotification()
            }
            requireActivity().onBackPressed()
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showNotification() {
        val channelId = "note_channel"
        val notificationId = 3
        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Notes Saved Successfully")
            .setContentText("You added a new note")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(requireContext())

        try {
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(notificationId, builder.build())
            } else {
                requestNotificationPermission()
            }
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "Permission required for notifications.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun requestNotificationPermission() {
        try {
            val notificationManager = NotificationManagerCompat.from(requireContext())
            if (!notificationManager.areNotificationsEnabled()) {
                val intent = Intent()
                intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                startActivity(intent)
            }
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(),"Notification settings not found.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun shareNote() {
        val title = binding.edtTitle.text.toString()
        val content = binding.edtNote.text.toString()

        if (title.isNotEmpty() && content.isNotEmpty()) {
            val noteText = "$title\n\n$content"

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, noteText)

            val shareIntentChooser = Intent.createChooser(shareIntent, "Share via")
            if (shareIntent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(shareIntentChooser)
            } else {
                Toast.makeText(
                    requireContext(),
                    "No apps available to handle sharing",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
    private fun shrinkFab(){
        binding.transparentBg.visibility = View.GONE
        binding.addNoteFeatures.startAnimation(rotateAntiClockWiseFabAnim)
        binding.addPriorityLevel.startAnimation(toBottomFabAnim)
        binding.addColor.startAnimation(toBottomFabAnim)
        binding.tvColor.startAnimation(toBottomFabAnim)
        binding.tvPriority.startAnimation(toBottomFabAnim)

        isExpanded = !isExpanded
    }

    private fun expandFab(){
        binding.transparentBg.visibility = View.VISIBLE
        binding.addNoteFeatures.startAnimation(rotateClockWiseFabAnim)
        binding.addPriorityLevel.startAnimation(fromBottomFabAnim)
        binding.addColor.startAnimation(fromBottomFabAnim)
        binding.tvColor.startAnimation(fromBottomFabAnim)
        binding.tvPriority.startAnimation(fromBottomFabAnim)

        isExpanded = !isExpanded
    }

    private fun showPriorityLevel() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val binding = PriorityLevelModalBinding.inflate(LayoutInflater.from(requireContext()))
        val bottomSheetView = binding.root

        binding.radioGroupPriority.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioHigh -> selectedPriority = NotePriority.HIGH
                R.id.radioMedium -> selectedPriority = NotePriority.MEDIUM
                R.id.radioLow -> selectedPriority = NotePriority.LOW
            }
        }

        binding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
    @SuppressLint("SuspiciousIndentation")
    private fun showColorPalette() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val binding = FragmentColorPaletteModalBinding.inflate(LayoutInflater.from(requireContext()))
        val bottomSheetView = binding.root

            binding.colorPicker.setOnColorSelectedListener { color ->
                handleColorSelection(color)
                bottomSheetDialog.dismiss()
            }

        binding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun handleColorSelection(selectedColor: Int) {
        binding.container.setBackgroundColor(selectedColor)
        this.selectedColor = selectedColor
    }

    fun onBackPressed() {
        if (isExpanded) {
            shrinkFab()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.allNotes.removeObservers(viewLifecycleOwner)
        fromBottomFabAnim.cancel()
        toBottomFabAnim.cancel()
        rotateClockWiseFabAnim.cancel()
        rotateAntiClockWiseFabAnim.cancel()
    }
}
