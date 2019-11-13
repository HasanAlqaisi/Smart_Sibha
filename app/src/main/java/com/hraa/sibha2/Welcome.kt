package com.hraa.sibha2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_welcome.*

class Welcome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        animationDO()
        Handler().postDelayed(
            {
                startActivity(Intent("android.intent.action.main2"))
                finish()
            },
            2700
        ) // after 2700 ms.. go to activity_main

    }

    private fun animationDO() {
        temp_symbol.animate().alpha(1f).duration = 2500
        temp_text.animate().rotation(360f).duration = 1250
    }
}
