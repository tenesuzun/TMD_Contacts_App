package com.example.tmdcontactsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R

class contactListAdapter: RecyclerView.Adapter<contactListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): contactListAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: contactListAdapter.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var contact_pp: ImageView
        var contact_firstname: TextView
        var contact_surname: TextView

        init {
            contact_pp = itemView.findViewById(R.id.contactPP)
            contact_firstname = itemView.findViewById(R.id.contactFirstName)
            contact_surname = itemView.findViewById(R.id.contactSurname)
        }

    }
}