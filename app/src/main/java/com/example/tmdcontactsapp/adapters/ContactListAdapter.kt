package com.example.tmdcontactsapp.adapters

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.models.Contact

class ContactListAdapter(private val listener: OnItemClickListener, private var contactsList: List<Contact>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: ArrayList<Contact>){
        contactsList = filteredList
        notifyDataSetChanged()
    }

   inner class ContactListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
       private val contactFullName: TextView = itemView.findViewById(R.id.contactFullName)
//       private val contactPicture: ImageView = itemView.findViewById(R.id.contactPP)

       init {
           itemView.setOnClickListener(this)
       }

       override fun onClick(v: View?) {
           val position: Int = adapterPosition
           if(position != RecyclerView.NO_POSITION){
           listener.onItemClick(position)
           }
       }

       @SuppressLint("SetTextI18n")
       fun bind(contact: Contact){
           contactFullName.text = contact.firstName + " " + contact.surname
//           if(contact.contactPicture == ""){
//               contactPicture.setImageResource(R.drawable.ic_round_account_box_24)
//           }else{
//               val imageBytes = Base64.decode(contact.contactPicture,0)
//               contactPicture.setImageBitmap(BitmapFactory.decodeByteArray(
//                   imageBytes,
//                   0,
//                   imageBytes.size
//               ))
//           }
       }
   }
   interface OnItemClickListener{
       fun onItemClick(position: Int)
   }
}
