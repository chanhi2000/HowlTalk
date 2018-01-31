package com.example.markiiimark.howltalk.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.markiiimark.howltalk.R
import com.example.markiiimark.howltalk.extension.replaceFragment
import com.example.markiiimark.howltalk.ui.fragment.PeopleFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(PeopleFragment(), R.id.frameLayout)
    }
}
