package com.example.markiiimark.howltalk.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.markiiimark.howltalk.R
import com.example.markiiimark.howltalk.extension.ctx
import com.example.markiiimark.howltalk.model.UserModel
import kotlinx.android.synthetic.main.item_friend.view.*

class PeopleAdapter(private val userModels: ArrayList<UserModel>,
                    private val onClick: ((UserModel) -> Unit)):
        RecyclerView.Adapter<PeopleAdapter.PersonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): PeopleAdapter.PersonViewHolder {
        val retView = LayoutInflater.from(parent.ctx)
                .inflate(R.layout.item_friend, parent, false)
        return PersonViewHolder(retView, onClick)
    }

    override fun onBindViewHolder(holder: PersonViewHolder,
                                  position: Int) {
        holder.bindUserModel(userModels[position])
    }

    override fun getItemCount(): Int = userModels.size

    inner class PersonViewHolder(val view: View,
                                 private val onClick: ((UserModel) -> Unit)) :
            RecyclerView.ViewHolder(view) {

        fun bindUserModel(model: UserModel) {
            with(model) {
                itemView.friendItemTextView.text = userName
                Glide.with(itemView.ctx)
                        .load(profileImageUrl)
                        .apply(RequestOptions().circleCrop())
                        .into(itemView.friendItemImageView)
                itemView.setOnClickListener {  onClick(model)  }
            }
        }
    }
}