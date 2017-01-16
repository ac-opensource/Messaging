package com.hyperion.messaging.main

import android.app.WallpaperManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import com.hyperion.messaging.MyApplication
import com.hyperion.messaging.R
import com.hyperion.messaging.flux.BaseActivity
import com.hyperion.messaging.util.ActivityExtensions.Companion.getScaledBitMapBasedOnScreenSize
import com.hyperion.messaging.util.BlurBuilder
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : BaseActivity() {

    companion object {
        fun startActivity(context: Context) {
            val intent: Intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyApplication.fluxComponent.inject(this)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayShowTitleEnabled(true)
            it.setDisplayUseLogoEnabled(true)
            it.title = "Messaging"
        }

        val wallpaperManager = WallpaperManager.getInstance(this)
        val wallpaperBitmap = (wallpaperManager.drawable as BitmapDrawable).bitmap
        backgroundBlurred.setImageBitmap(BlurBuilder.blur(this, getScaledBitMapBasedOnScreenSize(wallpaperBitmap)))
        supportFragmentManager.beginTransaction().add(R.id.containerView, ConversationsFragment()).commit()

    }

    override fun onResume() {
        super.onResume()

    }


}
