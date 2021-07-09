package com.example.tmdcontactsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.tmdcontactsapp.adapters.ContactListAdapter
import com.example.tmdcontactsapp.models.GroupResponse
import com.example.tmdcontactsapp.models.LoggedUserResponse
import com.example.tmdcontactsapp.networks.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val userArgEmail = "Email"
private const val userArgToken = "token"

class GroupListFragment : Fragment() {
    private var userEmail: String? = null
    private var userToken: String? = null
    private lateinit var groupsAdapter: ContactListAdapter
    private lateinit var groupsList: List<GroupResponse>

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

        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiClient::class.java)
        api.getUserByEmail(email = userEmail!!).enqueue(object: Callback<LoggedUserResponse>{
            override fun onResponse(call: Call<LoggedUserResponse>, response: Response<LoggedUserResponse>){
                when(response.code()){
                    200 -> {
                        api.getUserGroups(userId = response.body()!!.id).enqueue(object: Callback<List<GroupResponse>>{
                            override fun onResponse(call: Call<List<GroupResponse>>, response: Response<List<GroupResponse>>){
                                when(response.code()){
                                    200 ->{
                                        groupsList = response.body()!!
//                                        TODO(You will continue from here to make adapter for groups and respective item on click listener)
                                        groupsAdapter = ContactListAdapter(this@GroupListFragment, groupsList)
                                        val recycler = view.findViewById<RecyclerView>(R.id.fragmentGroupListRecycler)
                                        recycler?.apply {
                                            setHasFixedSize(true)
                                            layoutManager = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
                                            adapter = groupsAdapter
                                        }
                                    }
                                }
                            }

                            override fun onFailure(call: Call<List<GroupResponse>>, t: Throwable) {
//                                TODO("Not yet implemented")
                            }
                        })
                    }
                }
            }

            override fun onFailure(call: Call<LoggedUserResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })


        return view
    }
}