package com.example.kaihatu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class Shooting : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(SpaceShooter(this))
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
    }
}