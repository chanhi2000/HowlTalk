package com.example.markiiimark.howltalk.model

data class ChatModel(val users: HashMap<String, Boolean> = HashMap(),
                     val comments: HashMap<String,Comment> = HashMap()) {
    companion object {
        data class Comment(val uid:String?=null, val message:String?=null)
    }
}