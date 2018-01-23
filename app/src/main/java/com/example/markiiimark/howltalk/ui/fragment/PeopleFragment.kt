package com.example.markiiimark.howltalk.ui.fragment

import android.app.ActivityOptions
import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.constraint.solver.widgets.Snapshot
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.markiiimark.howltalk.R
import com.example.markiiimark.howltalk.model.UserModel
import com.example.markiiimark.howltalk.ui.activity.MessageActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PeopleFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val retView = inflater.inflate(R.layout.fragment_people, container, false)
        val recView = retView.findViewById<RecyclerView>(R.id.peopleFragRecView)
        recView.apply {
            layoutManager = LinearLayoutManager(inflater.context)
            adapter = PeopleFragmentRecyclerViewAdapter()
        }
        return retView
    }

    inner class PeopleFragmentRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var userModels = arrayListOf<UserModel>()

        init {
            FirebaseDatabase.getInstance().reference.child("users").addValueEventListener(object: ValueEventListener {
                override fun onCancelled(dbError: DatabaseError?) {

                }

                override fun onDataChange(dbSnapshot: DataSnapshot?) {
                    userModels.clear()
                    dbSnapshot!!.children.forEach { it ->
                        val userModel:UserModel? = it.getValue(UserModel::class.java)
                        userModels.add(userModel!!)
                    }
                    notifyDataSetChanged()
                }
            })
        }
        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int)
                : RecyclerView.ViewHolder {
            val retView = LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
            return PersonViewHolder(retView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

            val user = userModels.get(position)
            val mHolder = holder as PersonViewHolder

            Glide.with(holder.itemView.context)
                    .load(user.profileImageUrl)
                    .apply(RequestOptions().circleCrop())
                    .into(mHolder.imageView)
            mHolder.textView.text = user.userName
            mHolder.itemView.setOnClickListener {
                val intent = Intent(view.context, MessageActivity::class.java).apply {
                    putExtra("destinationUid", user.uid)
                }
                val activityOption: ActivityOptions?
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    activityOption = ActivityOptions.makeCustomAnimation(view.context, R.anim.from_right, R.anim.to_left)
                    startActivity(intent, activityOption.toBundle())
                }

            }
        }

        override fun getItemCount(): Int = userModels.size

        inner class PersonViewHolder(val view:View): RecyclerView.ViewHolder(view) {
            val imageView:ImageView by lazy {  view.findViewById(R.id.friendItemImageView) as ImageView }
            val textView:TextView by lazy {  view.findViewById(R.id.friendItemTextView) as TextView  }
        }
    }
}