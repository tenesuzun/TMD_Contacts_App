package com.example.tmdcontactsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.models.Contact

class ContactListAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var contactsList: List<Contact> = ArrayList()

    fun submitList(items: List<Contact>){
        contactsList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContactListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ContactListViewHolder ->{
                holder.bind(contactsList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return contactsList.size
    }

    class ContactListViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){
        private val contactFullName: TextView = itemView.findViewById(R.id.contactFullName)
        private val contactPP: ImageView = itemView.findViewById(R.id.contactPP)

        @SuppressLint("SetTextI18n")
        fun bind(contact: Contact){
            contactFullName.text = contact.firstName + " " + contact.surname
            contactPP.setImageResource(contact.contactPicture)
        }
    }

}
