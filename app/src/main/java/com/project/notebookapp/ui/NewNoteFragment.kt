package com.project.notebookapp.ui


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
import com.google.android.material.snackbar.Snackbar
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

class NewNoteFragment : Fragment() {

    private var _binding: FragmentNewNoteModalBinding? = null
    private val binding get() = _binding!!

    private val noteRepository: NoteRepository by inject()

    private val viewModel: NoteViewModel by viewModel { parametersOf(noteRepository)  }

    private var isExpanded = false

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewNoteModalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.saveButton.setOnClickListener {
            saveNote()
        }

        binding.shareButton.setOnClickListener {
            Toast.makeText(requireContext(), "Note Shared", Toast.LENGTH_SHORT).show()
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
    }

    private fun saveNote() {
        val totalNotes = viewModel.getTotalNotes()
        showNotification(totalNotes)

        val title = binding.edtTitle.text.toString()
        val content = binding.edtNote.text.toString()
        val timestamp = System.currentTimeMillis()

        if (checkText(title, content)) {
            val note = Note(title, content, timestamp, selectedPriority)

            viewModel.addNote(note)
            findNavController().navigate(NewNoteFragmentDirections.actionNewNoteModalToNoteFragment())
            Snackbar.make(binding.root, "Note saved successfully", Snackbar.LENGTH_SHORT).show()

        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkText(title: String, content: String): Boolean {
        return title.isNotEmpty() && content.isNotEmpty()
    }

//    private fun showNotification(totalNotes: Int) {
//        val channelId = "note_channel" // Use the same channel ID as created earlier
//        val notificationId = 3
//        val builder = NotificationCompat.Builder(requireContext(), channelId)
//            .setSmallIcon(R.drawable.ic_notification)
//            .setContentTitle("Total Notes")
//            .setContentText("You have $totalNotes notes.")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//    val notificationManager = NotificationManagerCompat.from(requireContext())
//
//    try {
//        if (notificationManager.areNotificationsEnabled()) {
//            notificationManager.notify(notificationId, builder.build())
//        } else {
//            val intent = Intent()
//            intent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
//            intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
//            startActivity(intent)
//
////            Snackbar.make(binding.root, "Notifications are disabled. Please enable them in settings.", Snackbar.LENGTH_LONG)
////                .apply {
////                    setAction("Enable") {
////                        requestNotificationPermission()
////                    }
////                        .show()
////                }
//        }
//    } catch (e: SecurityException) {
//        Toast.makeText(requireContext(),"Permission required for notifications.", Toast.LENGTH_SHORT).show()
//    }
//    }

    private fun showNotification(totalNotes: Int) {
        val channelId = "note_channel" // Use the same channel ID as created earlier
        val notificationId = 3
        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Total Notes")
            .setContentText("You have $totalNotes notes.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = NotificationManagerCompat.from(requireContext())

        try {
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(notificationId, builder.build())
            } else {
                requestNotificationPermission()

//                Snackbar.make(binding.root, "Notifications are disabled. Please enable them in settings.", Snackbar.LENGTH_LONG)
//                .apply {
//                    setAction("Enable") {
//                        requestNotificationPermission()
//                    }
//                        .show()
//                }
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

    private fun showPriorityLevel(){
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val binding = PriorityLevelModalBinding.inflate(LayoutInflater.from(requireContext()))
        val bottomSheetView = binding.root
        binding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
            val buttons = listOf(
                binding.radioHigh,
                binding.radioMedium,
                binding.radioLow
            )

            for (button in buttons) {
                button.setOnClickListener {
                    selectedPriority = when (button.id) {
                        R.id.radioHigh -> NotePriority.HIGH
                        R.id.radioMedium -> NotePriority.MEDIUM
                        R.id.radioLow -> NotePriority.LOW
                        else -> NotePriority.HIGH
                    }
                }
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.notes.removeObservers(viewLifecycleOwner)
        fromBottomFabAnim.cancel()
        toBottomFabAnim.cancel()
        rotateClockWiseFabAnim.cancel()
        rotateAntiClockWiseFabAnim.cancel()
    }

}