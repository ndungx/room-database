package com.ndungx.noteapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ndungx.noteapp.R

class NoteAddActivity : AppCompatActivity() {

    private lateinit var edtNoteTitle: EditText
    private lateinit var edtNoteDescription: EditText
    private lateinit var btnCancel: Button
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add)
        supportActionBar!!.title = "Add Note"

        edtNoteTitle = findViewById(R.id.edtNoteTitle)
        edtNoteDescription = findViewById(R.id.edtNoteDescription)
        btnCancel = findViewById(R.id.btnCancel)
        btnSave = findViewById(R.id.btnSave)

        btnCancel.setOnClickListener {
            Toast.makeText(applicationContext, "Nothing save", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnSave.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val noteTitle: String = edtNoteTitle.text.toString()
        val noteDescription: String = edtNoteDescription.text.toString()

        if (noteTitle.trim().isEmpty() || noteDescription.trim().isEmpty()) {
            Toast.makeText(applicationContext, "Please insert a title and description", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent()
        intent.putExtra("title", noteTitle)
        intent.putExtra("description", noteDescription)
        setResult(RESULT_OK, intent)

        finish()
    }
}