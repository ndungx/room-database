package com.ndungx.noteapp.view

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ndungx.noteapp.NoteApplication
import com.ndungx.noteapp.R
import com.ndungx.noteapp.adapter.NoteAdapter
import com.ndungx.noteapp.model.Note
import com.ndungx.noteapp.viewmodel.NoteViewModel
import com.ndungx.noteapp.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    private lateinit var addActivityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var updateActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val noteAdapter = NoteAdapter(this)
        recyclerView.adapter = noteAdapter

        // register for activity result
        registerActivityResultLauncher()

        val viewModelFactory = NoteViewModelFactory((application as NoteApplication).repository)

        noteViewModel = ViewModelProvider(this, viewModelFactory).get(NoteViewModel::class.java)
        noteViewModel.getAllNotes.observe(this, Observer { notes ->
            // Update the UI
            noteAdapter.setNote(notes)
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT /* or ItemTouchHelper.RIGHT*/) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = noteAdapter.getNote(viewHolder.adapterPosition)
                noteViewModel.delete(note)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                val right: Int = 1
                val left: Int = 0

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && isCurrentlyActive) {
                    val direction: Int = if (dX > 0) {
                        right
                    } else {
                        left
                    }

                    when (direction) {
                        left -> {
                            val itemView: View = viewHolder.itemView
                            val itemHeight = itemView.bottom - itemView.top

                            // Draw red background
                            val bg = ColorDrawable()
                            bg.color = Color.RED
                            bg.setBounds(
                                itemView.left,
                                itemView.top,
                                itemView.right,
                                itemView.bottom
                            )
                            bg.draw(c)

                            // Draw icon
                            val icon = ResourcesCompat.getDrawable(resources, R.drawable.delete_icon, null)
                            val intrinsicWidth = icon?.intrinsicWidth
                            val intrinsicHeight = icon?.intrinsicHeight
                            val deleteIconMargin = (itemHeight - intrinsicHeight!!) / 2

                            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
                            val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth!!
                            val deleteIconRight = itemView.right - deleteIconMargin
                            val deleteIconBottom = deleteIconTop + intrinsicHeight
                            icon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                            icon.draw(c)
                        }
                        right -> {}
                    }
                }
            }
        }).attachToRecyclerView(recyclerView)
    }

    private fun registerActivityResultLauncher() {
        addActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { resultAddNote ->
                val resultCode = resultAddNote.resultCode
                val data = resultAddNote.data

                if (resultCode == RESULT_OK && data != null) {
                    val title: String = data.getStringExtra("title").toString()
                    val description: String = data.getStringExtra("description").toString()

                    val note = Note(title, description)
                    noteViewModel.insert(note)
                }
            }
        )

        updateActivityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { resultUpdateNote ->
                val resultCode = resultUpdateNote.resultCode
                val data = resultUpdateNote.data

                if (resultCode == RESULT_OK && data != null) {
                    val id: Int = data.getIntExtra("noteId", -1)
                    val updatedTitle: String = data.getStringExtra("updatedTitle").toString()
                    val updatedDescription: String = data.getStringExtra("updatedDescription").toString()

                    val note = Note(updatedTitle, updatedDescription)
                    note.id = id
                    noteViewModel.update(note)
                }
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add_note -> {
                val intent = Intent(this, NoteAddActivity::class.java)
                addActivityResultLauncher.launch(intent)
            }
            R.id.item_delete_note -> showDeleteDialog()
        }

        return true
    }

    private fun showDeleteDialog() {
        val dialogDelete = AlertDialog.Builder(this)
        dialogDelete.setTitle("Delete all notes")
        dialogDelete.setMessage("Are you sure you want to delete all notes?")
        dialogDelete.setPositiveButton("Yes") { _, _ -> noteViewModel.deleteAllNotes() }
        dialogDelete.setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        dialogDelete.create().show()
    }
}