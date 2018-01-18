package com.example.markiiimark.howltalk.ui.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.markiiimark.howltalk.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class LoginActivity : AppCompatActivity() {
    companion object {
        val TAG = "LoginActivity"
    }

    private val idEditText by lazy {  findViewById(R.id.idEditText) as EditText  }
    private val pwEditText by lazy {  findViewById(R.id.pwEditText) as EditText  }
    private val loginButton by lazy {  findViewById(R.id.loginButton) as Button  }
    private val joinButton by lazy {  findViewById(R.id.joinButton) as Button  }

    private val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val mFirebaseAuth = FirebaseAuth.getInstance()
    private val mFirebaseAuthListener: FirebaseAuth.AuthStateListener

    init {
        mFirebaseAuthListener = FirebaseAuth.AuthStateListener { auth:FirebaseAuth ->
            val user = mFirebaseAuth.currentUser
            if (user != null) {
                // Login Success
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                // Login Failed
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // remove status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        setActivityLayout()
        mFirebaseAuth.signOut()
    }

    private fun setActivityLayout() {
        val splashBackground = mFirebaseRemoteConfig.getString("splash_background")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(splashBackground)
        }

        loginButton.setBackgroundColor(Color.parseColor(splashBackground))
        joinButton.setBackgroundColor(Color.parseColor(splashBackground))

    }

    fun joinButtonClicked(v:View) {
        Log.d(TAG, "joinButtonClicked")
        startActivity(Intent(this@LoginActivity, JoinActivity::class.java))
    }

    fun loginClicked(v:View) {
        val id = idEditText.text.toString()
        val pw = pwEditText.text.toString()
        mFirebaseAuth.signInWithEmailAndPassword(id, pw)
                .addOnCompleteListener { task: Task<AuthResult> ->
                    if(!task.isSuccessful) {
                        // login failed
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                    }
                }
    }


    override fun onStart() {
        super.onStart()
        mFirebaseAuth.addAuthStateListener(mFirebaseAuthListener)
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAuth.removeAuthStateListener(mFirebaseAuthListener)
    }
}
