package com.project.notebookapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.project.notebookapp.data.Note
import com.project.notebookapp.repo.FakeNoteRepository
import com.project.notebookapp.utils.NotePriority
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NoteViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var noteRepository: FakeNoteRepository
    private lateinit var viewModel: NoteViewModel

    @Before
    fun setup() {
        noteRepository = FakeNoteRepository()
        viewModel = NoteViewModel(noteRepository, StandardTestDispatcher(testDispatcher.scheduler))
    }

    @Test
    fun `saveNote should call saveNote function in repository with correct parameters`() {
        testScope.runBlockingTest {
            val note = Note(
                title = "Test Title",
                content = "Test Content",
                timestamp = System.currentTimeMillis(),
                priority = NotePriority.HIGH,
                color = 123
            )

            viewModel.saveNote(note)

            advanceUntilIdle()

            assertThat(noteRepository.savedNote).isEqualTo(note)
        }
    }

    @Test
    fun `updateNote should call updateNote function in repository with correct parameters`() {
        testScope.runBlockingTest {
            val existingNote = Note(
                title = "Test Title",
                content = "Test Content",
                timestamp = System.currentTimeMillis(),
                priority = NotePriority.HIGH,
                color = 123
            )

            viewModel.updateNote(existingNote)

            testScheduler.apply { advanceTimeBy(1000); runCurrent() }

            assertThat(noteRepository.updatedNote).isEqualTo(existingNote)
        }
    }

    @Test
    fun `deleteNote should call deleteNote function in repository with correct parameters`() {
        testScope.runBlockingTest {
            val noteToDelete = Note(
                title = "Test Title",
                content = "Test Content",
                timestamp = System.currentTimeMillis(),
                priority = NotePriority.HIGH,
                color = 123
            )

            viewModel.deleteNote(noteToDelete)

            testScheduler.apply { advanceTimeBy(1000); runCurrent() }

            assertThat(noteRepository.deletedNote).isEqualTo(noteToDelete)
        }
    }

    @Test
    fun `getNoteById should call getNoteById function in repository with correct parameters`() {
        testScope.runBlockingTest {
            val noteId = 42L
            viewModel.getNoteById(noteId)
            assertThat(noteRepository.getNoteByIdCalled).isTrue()
            assertThat(noteRepository.noteIdToRetrieve).isEqualTo(noteId)
        }
    }

    @Test
    fun `allNotes should return LiveData from repository`() {
        noteRepository.setSampleNote()
        val viewModelLiveData = viewModel.allNotes
        assertThat(viewModelLiveData).isSameInstanceAs(noteRepository.allNotes)
    }

    @Test
    fun `searchNotes should call searchNotes function in repository with correct parameters`() {
        testScope.runBlockingTest {
            val query = "TestQuery"
            viewModel.searchNotes(query)
            assertThat(noteRepository.searchNotesCalled).isTrue()
            assertThat(noteRepository.searchQuery).isEqualTo(query)
        }
    }

}