package com.example.markiiimark.howltalk

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class LoginActivity : AppCompatActivity() {

    private val loginButton by lazy {  findViewById(R.id.loginButton) as Button  }
    private val joinButton by lazy {  findViewById(R.id.joinButton) as Button  }
    private val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        // remove status bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setActivityLayout()
    }

    private fun setActivityLayout() {
        val splashBackground = mFirebaseRemoteConfig.getString("splash_background")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.parseColor(splashBackground)
        }

        loginButton.setBackgroundColor(Color.parseColor(splashBackground))
        joinButton.setBackgroundColor(Color.parseColor(splashBackground))
    }
}
