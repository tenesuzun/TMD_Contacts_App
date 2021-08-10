package com.example.tmdcontactsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.models.Contact
import kotlinx.android.synthetic.main.contact_list_item_row.*
import kotlinx.android.synthetic.main.contact_list_item_row.view.*

class ContactListAdapter(private val listener: OnItemClickListener, private var contactsList: List<Contact>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var selectedPosition: Int = -1
    private var isViewClosed: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ContactListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ContactListViewHolder ->{
                if(isViewClosed){
                    holder.itemView.contactsListOptionsMenu.visibility = View.VISIBLE
                    holder.itemView.contactsListCallBtn.visibility = View.VISIBLE
                    holder.itemView.contactsListDetailsBtn.visibility = View.VISIBLE
                    holder.itemView.contactFullName.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow_up_24,0)

                    isViewClosed = false

                }else{
                    holder.itemView.contactsListOptionsMenu.visibility = View.GONE
                    holder.itemView.contactsListCallBtn.visibility = View.GONE
                    holder.itemView.contactsListDetailsBtn.visibility = View.GONE
                    holder.itemView.contactFullName.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_arrow_down_24,0)

                    isViewClosed = true
                }
            holder.bind(contactsList[position])
        }
    }
}

    override fun getItemCount(): Int {
        return contactsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Contact>){
        contactsList = filteredList
        notifyDataSetChanged()
    }

   inner class ContactListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
       private val contactFullName: TextView = itemView.findViewById(R.id.contactFullName)

       init {
           itemView.setOnClickListener(this)
       }

       override fun onClick(v: View?) {
           val position: Int = adapterPosition
           notifyItemChanged(position)
           if(position != RecyclerView.NO_POSITION){
           listener.onItemClick(position)
           }
       }

       @SuppressLint("SetTextI18n")
       fun bind(contact: Contact){
           contactFullName.text = contact.firstName + " " + contact.surname
       }
   }

   interface OnItemClickListener{
       fun onItemClick(position: Int)
   }
}