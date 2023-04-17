package com.ndungx.noteapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ndungx.noteapp.R

class NoteUpdateActivity : AppCompatActivity() {

    private lateinit var edtNoteTitleUpdate: EditText
    private lateinit var edtNoteDescriptionUpdate: EditText
    private lateinit var btnCancelUpdate: Button
    private lateinit var btnUpdate: Button

    private var currentId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_update)
        supportActionBar!!.title = "Update Note"

        edtNoteTitleUpdate = findViewById(R.id.edtNoteTitleUpdate)
        edtNoteDescriptionUpdate = findViewById(R.id.edtNoteDescriptionUpdate)
        btnCancelUpdate = findViewById(R.id.btnCancelUpdate)
        btnUpdate = findViewById(R.id.btnUpdate)

        getAndSetData()

        btnCancelUpdate.setOnClickListener {
            Toast.makeText(applicationContext, "Nothing update", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnUpdate.setOnClickListener {
            updateNote()
        }
    }

    private fun updateNote() {
        val updatedTitle = edtNoteTitleUpdate.text.toString()
        val updatedDescription = edtNoteDescriptionUpdate.text.toString()

        if (updatedTitle.isEmpty() || updatedDescription.isEmpty()) {
            Toast.makeText(applicationContext, "Please insert title and description", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent()
        intent.putExtra("updatedTitle", updatedTitle)
        intent.putExtra("updatedDescription", updatedDescription)

        if (currentId != -1) {
            intent.putExtra("noteId", currentId)

            setResult(RESULT_OK, intent)
            finish()
        } else {
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun getAndSetData() {
        currentId = intent.getIntExtra("currentId", -1)
        val currentTitle = intent.getStringExtra("currentTitle")
        val currentDescription = intent.getStringExtra("currentDescription")

        edtNoteTitleUpdate.setText(currentTitle)
        edtNoteDescriptionUpdate.setText(currentDescription)
    }
}