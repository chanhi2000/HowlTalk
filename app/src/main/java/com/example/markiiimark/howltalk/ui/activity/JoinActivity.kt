package com.example.markiiimark.howltalk.ui.activity

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.markiiimark.howltalk.R
import com.example.markiiimark.howltalk.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class JoinActivity() : AppCompatActivity() {

    private val emailEditText by lazy {  findViewById(R.id.emailEditText) as EditText  }
    private val nameEditText by lazy {  findViewById(R.id.nameEditText) as EditText  }
    private val pwEditText by lazy {  findViewById(R.id.pwEditText) as EditText  }
    private val joinButton by lazy {  findViewById(R.id.joinButton) as Button  }
    private val profileImageView by lazy {  findViewById(R.id.profileImageView) as ImageView }
    private var imageUri: Uri? = null

    private val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val splashBackground = mFirebaseRemoteConfig.getString(getString(R.string.rc_color))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(splashBackground)
        }
        joinButton.setBackgroundColor(Color.parseColor(splashBackground))
    }

    fun registerButtonClicked(v: View) {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val pw = pwEditText.text.toString()

        if (name.isEmpty() ||
                email.isEmpty() ||
                pw.isEmpty() ||
                imageUri == null) {  return  }
        registerUserToDatabase(email, pw, name, imageUri)
    }

    private fun registerUserToDatabase(email: String,
                                       password: String,
                                       name: String,
                                       uri: Uri?) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->

                    val uid:String = task.result.user.uid
                    FirebaseStorage.getInstance().reference.child("userImages").child(uid)
                            .putFile(uri!!)
                            .addOnCompleteListener { task2: Task<UploadTask.TaskSnapshot> ->
                                val imageUrl:String = task2.result.downloadUrl.toString()
                                val userModel = UserModel(name, imageUrl)
                                FirebaseDatabase.getInstance().reference
                                        .child("users").child(uid).setValue(userModel).addOnCompleteListener {
                                    this@JoinActivity.finish()
                                }
                    }
                }
    }

    fun profileImageViewClicked(v:View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE)
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        if (requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK) {
            profileImageView.setImageURI(data?.data as Uri)
            imageUri = data.data
        }
    }

    companion object {
        private val PICK_FROM_ALBUM:Int = 10
    }
}
