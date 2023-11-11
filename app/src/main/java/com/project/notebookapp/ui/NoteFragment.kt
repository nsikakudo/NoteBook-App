package com.project.notebookapp.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
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
import java.util.concurrent.TimeUnit

class NoteFragment : Fragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!

    private val noteRepository: NoteRepository by inject()

    private val viewModel: NoteViewModel by viewModel { parametersOf(noteRepository) }

    private lateinit var rvAdapter: NoteAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvAdapter = NoteAdapter(object : NoteClickListener {
            override fun editNote(note: Note) {
                val bundle = bundleOf("noteId" to note.id)
                findNavController().navigate(
                    R.id.action_noteFragment_to_newNoteModal,
                    bundle
                )
            }
        })

        displayRecyclerView()

        val recyclerView = binding.noteRecyclerView
        recyclerView.adapter = rvAdapter

        viewModel.allNotes.observe(viewLifecycleOwner) { notes ->
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
            showAboutUsDialog()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.searchNotes(it).observe(viewLifecycleOwner) { notes ->
                        rvAdapter.submitList(notes)
                    }
                }
                return true
            }
        })

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
                            viewModel.saveNote(note)
                        }
                            .show()
                    }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.noteRecyclerView)
        }

    }

    private fun showAboutUsDialog() {
        val binding = FragmentAboutUsBinding.inflate(LayoutInflater.from(requireContext()))
        val dialog = AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setPositiveButton(getString(R.string.about_us_ok_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        binding.tvTitle.text = getString(R.string.about_us_title)
        binding.tvNote.text = getString(R.string.about_us_intro)
        binding.tvContent.text = getString(R.string.about_us_content)

        binding.imgTwitter.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com"))
            startActivity(intent)
        }

        binding.imgFacebook.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"))
            startActivity(intent)
        }

        binding.imgInstagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"))
            startActivity(intent)
        }

        dialog.show()
    }
    private fun displayRecyclerView(){
        when(resources.configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> setUpRecyclerView(2)
            Configuration.ORIENTATION_LANDSCAPE -> setUpRecyclerView(3)
        }
    }

    private fun setUpRecyclerView(spanCount: Int) {
    binding.noteRecyclerView.apply {
        layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
        setHasFixedSize(true)
        adapter = rvAdapter
        postponeEnterTransition(300L, TimeUnit.MILLISECONDS)
        viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
    }
}


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.allNotes.removeObservers(viewLifecycleOwner)
        binding.noteRecyclerView.adapter = null
    }

}


