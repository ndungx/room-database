package com.ndungx.noteapp.repository

import androidx.annotation.WorkerThread
import com.ndungx.noteapp.model.Note
import com.ndungx.noteapp.room.NoteDAO
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDAO) {
    val getAllNotes: Flow<List<Note>> = noteDao.getAllNotes()

    @WorkerThread
    suspend fun insert(note: Note) = noteDao.insert(note)

    @WorkerThread
    suspend fun update(note: Note) = noteDao.update(note)

    @WorkerThread
    suspend fun delete(note: Note) = noteDao.delete(note)

    @WorkerThread
    suspend fun deleteAllNotes() = noteDao.deleteAllNotes()
}