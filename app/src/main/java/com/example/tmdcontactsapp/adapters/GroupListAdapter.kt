package com.example.tmdcontactsapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdcontactsapp.R
import com.example.tmdcontactsapp.models.GroupResponse

class GroupListAdapter(private val listener: OnItemClickListener, private var groupsList: List<GroupResponse>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return GroupListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.group_list_item_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is GroupListViewHolder ->{
                holder.bind(groupsList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return groupsList.size
    }

    fun filterList(filteredList: ArrayList<GroupResponse>){
        groupsList = filteredList
        notifyDataSetChanged()
    }

    inner class GroupListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
        private val groupName: TextView = itemView.findViewById(R.id.groupName)

        init{
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if(position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(group: GroupResponse){
            groupName.text = group.groupName
        }
    }

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }
}