package com.project.notebookapp.di

import com.project.notebookapp.repo.NoteRepository
import com.project.notebookapp.repo.NoteRepositoryImpl
import com.project.notebookapp.ui.viewmodel.NoteViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<NoteRepository> { NoteRepositoryImpl() }
    viewModel { (noteRepository: NoteRepository) -> NoteViewModel(noteRepository) }
}