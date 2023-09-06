package com.example.kaihatu.game

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.kaihatu.MainActivity
import com.example.kaihatu.R
import com.example.kaihatu.model.Stone
import com.example.kaihatu.osero
import io.realm.Realm
import io.realm.kotlin.where

class GameResultActivity : AppCompatActivity() {
    private lateinit var realm: Realm

    companion object {
        const val EXTRA_PLAYER = "extra_player"
        const val EXTRA_BLACK_COUNT = "extra_black_count"
        const val EXTRA_WHITE_COUNT = "extra_white_count"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getDefaultInstance()
        setContentView(R.layout.activity_game_result)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("ミニゲーム")
        }

        val player = intent.getSerializableExtra(EXTRA_PLAYER) as Stone
        val blackCount = intent.getIntExtra(EXTRA_BLACK_COUNT, 0)
        val whiteCount = intent.getIntExtra(EXTRA_WHITE_COUNT, 0)

        val winnerTextView: TextView = findViewById(R.id.winnerTextView)
        val blackCountTextView: TextView = findViewById(R.id.blackCountTextView)
        val whiteCountTextView: TextView = findViewById(R.id.whiteCountTextView)

        val winnerText = when (player) {
            Stone.BLACK -> getString(R.string.text_winner_black)
            Stone.WHITE -> getString(R.string.text_winner_white)
            else -> throw IllegalArgumentException()
        }

        when (player) {
            Stone.BLACK -> updateData_win()
            Stone.WHITE -> updateData()
            else -> throw IllegalArgumentException()
        }

        winnerTextView.text = winnerText
        blackCountTextView.text = getString(R.string.black_text_count, blackCount)
        whiteCountTextView.text = getString(R.string.white_text_count, whiteCount)

        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener {
            // メイン画面に戻るためのIntentを作成して起動
            val intent = Intent(this, osero::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun updateData() {
        val data = realm.where<Data>()
                .equalTo("id",1.toInt())
                .findFirst()
        realm.executeTransaction {
            var win_osero_white = realm.where<Data>().max("win_osero_white")
            win_osero_white = (win_osero_white?.toLong() ?: 0) + 1
            data?.win_osero_white = win_osero_white?.toInt()
        }

        Log.e("RealmUpdate", "更新しました:${realm.where<Data>().equalTo("id", 1.toInt()).findFirst()}")
    }

    private fun updateData_win() {
        val data = realm.where<Data>()
                .equalTo("id",1.toInt())
                .findFirst()
        realm.executeTransaction {
            var win_osero_black= realm.where<Data>().max("win_osero_black")
            win_osero_black = (win_osero_black?.toLong() ?: 0) + 1
            data?.win_osero_black = win_osero_black?.toInt()

        }

        Log.e("RealmUpdate", "更新しました:${realm.where<Data>().equalTo("id", 1.toInt()).findFirst()}")
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
        }

        return true
    }
}