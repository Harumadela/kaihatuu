package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.test.ai.OseroAI
import com.example.test.game.GameActivity
import com.example.test.model.ai.AINone
import com.example.test.model.ai.AIStrong
import com.example.test.model.ai.AIWeak


class osero : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.osero)

        // アクションバーに戻るを表示する
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("オセロ")
        }

        val topModeManualButton = findViewById<Button>(R.id.topModeManualButton)
        val topModeAIWeakButton = findViewById<Button>(R.id.topModeAIWeakButton)
        val topModeAIStrongButton = findViewById<Button>(R.id.topModeAIStrongButton)
        topModeManualButton.setOnClickListener { startGameActivity(AINone()) }
        topModeAIWeakButton.setOnClickListener { startGameActivity(AIWeak()) }
        topModeAIStrongButton.setOnClickListener { startGameActivity(AIStrong()) }
    }
    private fun startGameActivity(ai: OseroAI) {
        startActivity(GameActivity.createIntent(this, ai))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.action_user ->{
                var intent = Intent(this, Account::class.java)
                startActivity(intent)
            }
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}