package com.ndungx.noteapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.ndungx.noteapp.R
import com.ndungx.noteapp.model.Note
import com.ndungx.noteapp.view.MainActivity
import com.ndungx.noteapp.view.NoteUpdateActivity

class NoteAdapter(private val activity: MainActivity) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    private var notes: List<Note> = ArrayList()

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDescription: TextView = itemView.findViewById(R.id.txtDescription)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote: Note = notes[position]
        holder.txtTitle.text = currentNote.title
        holder.txtDescription.text = currentNote.description

        holder.cardView.setOnClickListener {
            val intent = Intent(activity, NoteUpdateActivity::class.java)
            intent.putExtra("currentId", currentNote.id)
            intent.putExtra("currentTitle", currentNote.title)
            intent.putExtra("currentDescription", currentNote.description)
            activity.updateActivityResultLauncher.launch(intent)
        }
    }

    fun setNote(myNotes: List<Note>) {
        this.notes = myNotes
        notifyDataSetChanged()
    }

    fun getNote(position: Int): Note = notes[position]
}