package com.example.tmdcontactsapp

import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.tmdcontactsapp.models.GroupResponse
import com.example.tmdcontactsapp.networks.ApiClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GroupAddDialogFragment(private val tempTitle: String, private val tempHint: String, private val userId: Int) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view = requireActivity().layoutInflater.inflate(R.layout.fragment_group_add_dialog, null)
        view.findViewById<TextView>(R.id.groupAddDialogTitle).text = tempTitle
        view.findViewById<EditText>(R.id.groupAddDialogEditText).hint = tempHint

        return AlertDialog.Builder(requireContext())
            .setView(view)
            .setPositiveButton(getString(R.string.dialogPositive)){_,_ ->
                val tempName = view.findViewById<EditText>(R.id.groupAddDialogEditText).text.toString()
                postGroup(tempName)
            }
            .setNegativeButton(getString(R.string.dialogNegative)){dialog,_ -> dialog.dismiss()}
            .create()
    }

    private fun postGroup(tempName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://tmdcontacts-api.dev.tmd/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiClient::class.java).addNewGroup(GroupResponse(groupId = 0,groupName = tempName,userId = userId))
            .enqueue(object : Callback<ResponseBody>{
                override fun onResponse(call: Call<ResponseBody>,response: Response<ResponseBody>) {
                    when(response.code()){
                        200 -> {
                            Toast.makeText(context,"Group is created successfully", Toast.LENGTH_LONG).show()
                            dismiss()
                        }else -> {
                            Toast.makeText(context,"Unexpected problem",Toast.LENGTH_LONG).show()
                            dismiss()
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(context,"onFailure?",Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            })
    }
}

