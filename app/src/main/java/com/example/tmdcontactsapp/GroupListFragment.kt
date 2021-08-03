package com.example.tmdcontactsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tmdcontactsapp.adapters.GroupListAdapter
import com.example.tmdcontactsapp.models.GroupResponse
import com.example.tmdcontactsapp.models.LoggedUserResponse
import com.example.tmdcontactsapp.models.ResponseContent
import com.example.tmdcontactsapp.networks.ApiClient
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val userArgEmail = "Email"
private const val userArgToken = "token"

class GroupListFragment : Fragment(), GroupListAdapter.OnItemClickListener {
    private var userEmail: String? = null
    private var userToken: String? = null
    private lateinit var groupsAdapter: GroupListAdapter
    private lateinit var groupsList: MutableList<GroupResponse>
    private var filteredList: ArrayList<GroupResponse> = ArrayList()
    private var userId: Int = 0

    fun newInstance(bundle: Bundle): GroupListFragment{
        val fragment = GroupListFragment()
        fragment.arguments = bundle
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle: Bundle = arguments?.getBundle("bundle")!!
        bundle.let {
            userEmail = it.getString(userArgEmail)
            userToken = it.getString(userArgToken)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_group_list, container, false)
        val searchBar = view.findViewById<TextInputEditText>(R.id.groupsSearchBarField)
        searchBar.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                Toast.makeText(context, "BeforeTextChanged",Toast.LENGTH_SHORT).show()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                Toast.makeText(context, "OnTextChanged",Toast.LENGTH_SHORT).show()
            }

            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })

        val swipeAction = object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Retrofit.Builder().baseUrl("http://tmdcontacts-api.dev.tmd/api/").addConverterFactory(GsonConverterFactory.create()).build()
                    .create(ApiClient::class.java).deleteGroup(groupId = groupsList[viewHolder.adapterPosition].groupId, Bearer = "Bearer $userToken")
                    .enqueue(object: Callback<ResponseContent>{
                        @SuppressLint("NotifyDataSetChanged")
                        override fun onResponse(
                            call: Call<ResponseContent>,
                            response: Response<ResponseContent>
                        ) {
                            when(response.code()){
                                200 -> {
                                    Toast.makeText(context,response.body()!!.message, Toast.LENGTH_SHORT).show()
                                    groupsList.removeAt(viewHolder.adapterPosition)
                                    groupsAdapter.notifyDataSetChanged()
                                }else -> {
                                    Toast.makeText(context,response.body()!!.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                        override fun onFailure(call: Call<ResponseContent>, t: Throwable) {
                            Toast.makeText(context,"onFailure", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)
        api.getUserByEmail(email = userEmail!!, Bearer = "Bearer $userToken").enqueue(object: Callback<LoggedUserResponse>{
            override fun onResponse(call: Call<LoggedUserResponse>, response: Response<LoggedUserResponse>){
                when(response.code()){
                    200 -> {
                        userId = response.body()!!.id
                        api.getUserGroups(userId = userId, Bearer = "Bearer $userToken").enqueue(object: Callback<MutableList<GroupResponse>>{
                            override fun onResponse(call: Call<MutableList<GroupResponse>>, response: Response<MutableList<GroupResponse>>){
                                when(response.code()){
                                    200 ->{
                                        groupsList = response.body()!!
                                        groupsAdapter = GroupListAdapter(this@GroupListFragment, groupsList)
                                        val recycler = view.findViewById<RecyclerView>(R.id.fragmentGroupListRecycler)
                                        recycler?.apply {
                                            setHasFixedSize(true)
                                            layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
                                            adapter = groupsAdapter
                                            ItemTouchHelper(swipeAction).attachToRecyclerView(recycler)
                                        }
                                    }
                                    400 -> {
                                        Toast.makeText(context,"This user does not have groups",Toast.LENGTH_SHORT).show()
                                    } else -> {
                                        Toast.makeText(context,response.code().toString(), Toast.LENGTH_SHORT).show()
                                        Toast.makeText(context,response.message().toString(), Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                            override fun onFailure(call: Call<MutableList<GroupResponse>>, t: Throwable) {
                                Toast.makeText(context,"Either cellular or server is down", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else ->{
                    Toast.makeText(context,"Could not get logged user details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<LoggedUserResponse>, t: Throwable) {
                Toast.makeText(context,"Either cellular or server is down",Toast.LENGTH_SHORT).show()
            }
        })

        view.findViewById<FloatingActionButton>(R.id.groupListFAB).setOnClickListener {
            activity?.let { it1 ->
                GroupAddDialogFragment("Create a new group?", "Write group name here", userId, userToken!!).show(
                    it1.supportFragmentManager, "GroupAddDialogFragment"
                )
            }
        }

        return view
    }

    fun filter(text: String){
        filteredList.clear()
        for(item: GroupResponse in groupsList){
            if(item.groupName.lowercase().contains(text.lowercase())){
                filteredList.add(item)
            }
        }
        groupsAdapter.filterList(filteredList)
    }

    override fun onItemClick(position: Int) {
        val clickedItem: GroupResponse = if(filteredList.isNotEmpty()){
            filteredList[position]
        } else{
            groupsList[position]
        }
        startActivity(Intent(context, DetailedGroupListActivity::class.java)
            .putExtra("groupId",clickedItem.groupId)
            .putExtra("userId",userId)
            .putExtra("token", userToken.toString()))
    }
}