package com.project.notebookapp.di

import androidx.room.Room
import com.project.notebookapp.data.NoteDataBase
import com.project.notebookapp.repo.NoteRepository
import com.project.notebookapp.repo.NoteRepositoryImpl
import com.project.notebookapp.ui.viewmodel.NoteViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            NoteDataBase::class.java, "note_database"
        ).build()
    }
    single { get<NoteDataBase>().noteDao() }
    single<NoteRepository> { NoteRepositoryImpl(get()) }
    viewModel { (noteRepository: NoteRepository) -> NoteViewModel(noteRepository) }
}
