package com.example.markiiimark.howltalk.ui.fragment

import android.app.ActivityOptions
import android.support.v4.app.Fragment
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.markiiimark.howltalk.R
import com.example.markiiimark.howltalk.model.UserModel
import com.example.markiiimark.howltalk.ui.activity.MessageActivity
import com.example.markiiimark.howltalk.ui.adapter.PeopleAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PeopleFragment: Fragment() {
    private val peopleRecyclerView by lazy {  activity.findViewById<RecyclerView>(R.id.peopleRecyclerView) }
    private var userModels = arrayListOf<UserModel>()
    private val peopleAdapter =
            PeopleAdapter(userModels) { userModel ->
                with(userModel) {
                    val intent = Intent(activity, MessageActivity::class.java).apply {
                        putExtra("destinationUid", uid)
                    }
                    val activityOption: ActivityOptions?
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        activityOption = ActivityOptions.makeCustomAnimation(activity, R.anim.from_right, R.anim.to_left)
                        startActivity(intent, activityOption.toBundle())
                    }
                }
            }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
            val retView = inflater.inflate(R.layout.fragment_people, container, false)
            peopleRecyclerView.layoutManager = LinearLayoutManager(inflater.context)
            setupUserModels()
            return retView
    }

    private fun setupUserModels() {
        val myUid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child("users").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(dbError: DatabaseError?) {
            }

            override fun onDataChange(dbSnapshot: DataSnapshot?) {
                userModels.clear()
                dbSnapshot!!.children.forEach { snapshot:DataSnapshot ->
                    val userModel = snapshot.getValue(UserModel::class.java) as UserModel
                    if (userModel.uid.equals(myUid)) return@forEach
                    userModels.add(userModel)
                }
                peopleRecyclerView.adapter = peopleAdapter
                peopleAdapter.notifyDataSetChanged()
            }
        })
    }
}