package com.ndungx.noteapp;

import android.app.Application
import com.ndungx.noteapp.repository.NoteRepository
import com.ndungx.noteapp.room.NoteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class NoteApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    private val database by lazy { NoteDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { NoteRepository(database.getNoteDao()) }
}
