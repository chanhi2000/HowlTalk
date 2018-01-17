package com.example.markiiimark.howltalk

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.example.markiiimark.howltalk.model.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class JoinActivity : AppCompatActivity() {

    private val emailEditText by lazy {  findViewById(R.id.emailEditText) as EditText  }
    private val nameEditText by lazy {  findViewById(R.id.nameEditText) as EditText  }
    private val pwEditText by lazy {  findViewById(R.id.pwEditText) as EditText  }
    private val joinButton by lazy {  findViewById(R.id.joinButton) as Button  }

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

        if (name.isEmpty() || email.isEmpty() || pw.isEmpty()) {  return  }
        registerUserToDatabase(email, pw, name)
    }

    private fun registerUserToDatabase(email: String,
                                       password: String,
                                       name: String) {
        FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    val userModel = UserModel(name)
                    val uid:String = task.result.user.uid
                    FirebaseDatabase.getInstance().reference
                            .child("users").child(uid).setValue(userModel)
                }
    }

}
