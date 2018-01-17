package com.example.markiiimark.howltalk

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

class SplashActivity: AppCompatActivity() {

    private var mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    private val splashLayout by lazy {  findViewById(R.id.SplashLayout) as LinearLayout  }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val configSetting = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        mFirebaseRemoteConfig.apply {
            setConfigSettings(configSetting)
            setDefaults(R.xml.default_config)
        }
        mFirebaseRemoteConfig.fetch(0L)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mFirebaseRemoteConfig.activateFetched()
                    } else {

                    }
                    displayMessage()
                }
    }

    private fun displayMessage() {
        val splashBackground:String = mFirebaseRemoteConfig.getString("splash_background")
        val caps:Boolean = mFirebaseRemoteConfig.getBoolean("splash_message_caps")
        val splashMessage:String = mFirebaseRemoteConfig.getString("splash_message")

        splashLayout.setBackgroundColor(Color.parseColor(splashBackground))
        if (caps) {
            val builder = AlertDialog.Builder(this@SplashActivity)
            builder.apply {
                setMessage(splashMessage)
                setPositiveButton("okay", { dialog:DialogInterface, id:Int ->
                    // onClick Method
                    finish()
                })
                create()
            }
            builder.show()
        }
    }
}