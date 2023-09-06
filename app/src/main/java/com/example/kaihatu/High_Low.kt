package com.example.test

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_high_low.*

class High_Low : AppCompatActivity() {
    private var yourCard = 0
    private var droidCard = 0
    private var hitCount = 0
    private var loseCount = 0
    private var gameStart = false
    private var answered = false
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_low)
        realm = Realm.getDefaultInstance()
        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("High＆Low")
        }
        drawCard()

        // アクションバーに戻るを表示する
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        // resetButtonを非表示
        resetSNextButton()

        highBtn.setOnClickListener {
            if (gameStart && !answered) {
                highAndLow('h')
                answered = true
                showNextButton()
            }
        }

        lowBtn.setOnClickListener {
            if (gameStart && !answered) {
                highAndLow('l')
                answered = true
                showNextButton()
            }
        }

        nextBtn.setOnClickListener {
            if (gameStart) {
                drawCard()
                resultText.text = ""
                answered = false
                hideNextButton()
            }
        }

        // restartボタンが押された時
        resetButton.setOnClickListener {
            finishAndRemoveTask();
            startActivity(intent);
        }
    }

    override fun onResume() {
        super.onResume()
        hitText.text = getString(R.string.hit_text)
        loseText.text = getString(R.string.lose_text)
        gameStart = true
        hideNextButton()
    }

    private fun drawCard() {
        yourCardImage.setImageResource(R.drawable.set)
        droidCardImage.setImageResource(R.drawable.set)
        yourCard = (1..13).random()
        showyourCard(yourCard)
    }

    private fun highAndLow(answer: Char) {
        droidCard = (1..13).random()
        showdroidCard(droidCard)
        val balance = yourCard - droidCard
        if (balance == 0) {
            resultText.text = "引き分け"
        } else if (balance > 0 && answer == 'h') {
            resultText.text = "当たりです"
            hitCount++
            hitText.text = getString(R.string.hit_text) + " "+ hitCount
        } else if (balance < 0 && answer == 'l') {
            resultText.text = "当たりです"
            hitCount++
            hitText.text = getString(R.string.hit_text) + " " + hitCount
        } else {
            resultText.text = "はずれです"
            loseCount++
            loseText.text = getString(R.string.lose_text) + " " + loseCount
        }

        if (hitCount == 5) {
            resultText.text = "あなたの勝ちです"
            resultText.setTextColor(Color.rgb(255, 0, 0))
            gameStart = false            // resetButtonを表示
            updateData_win()
            resetVNextButton() // Restart the game
            showNextButton()

        } else if (loseCount == 5) {
            resultText.text = "あなたの負けです"
            resultText.setTextColor(Color.rgb(0, 0, 255))
            gameStart = false
            updateData()
            // resetButtonを表示
            resetVNextButton() // Restart the game
            showNextButton()

        }
    }

    private fun showyourCard(number: Int) {
        when (number) {

            1 -> yourCardImage.setImageResource((R.drawable.spade1))
            2 -> yourCardImage.setImageResource((R.drawable.spade2))
            3 -> yourCardImage.setImageResource((R.drawable.spade3))
            4 -> yourCardImage.setImageResource((R.drawable.spade4))
            5 -> yourCardImage.setImageResource((R.drawable.spade5))
            6 -> yourCardImage.setImageResource((R.drawable.spade6))
            7 -> yourCardImage.setImageResource((R.drawable.spade7))
            8 -> yourCardImage.setImageResource((R.drawable.spade8))
            9 -> yourCardImage.setImageResource((R.drawable.spade9))
            10 -> yourCardImage.setImageResource((R.drawable.spade10))
            11 -> yourCardImage.setImageResource((R.drawable.spade11))
            12 -> yourCardImage.setImageResource((R.drawable.spade12))
            13 -> yourCardImage.setImageResource((R.drawable.spade13))
        }
    }

    private fun showdroidCard(number: Int) {
        var set = AnimationSet(true)
        var scale = ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, 160 / 2f, 160 / 2f)

        set.fillAfter = true
        set.isFillEnabled = true
        set.addAnimation(scale)
        set.duration = 100
        set.interpolator = LinearInterpolator()
        droidCardImage.startAnimation(set)


        Handler(Looper.getMainLooper()).postDelayed({
            // もう一度90°回転
            // もう一度90°回転
            when (number) {
                1 -> droidCardImage.setImageResource((R.drawable.heart1))
                2 -> droidCardImage.setImageResource((R.drawable.heart2))
                3 -> droidCardImage.setImageResource((R.drawable.heart3))
                4 -> droidCardImage.setImageResource((R.drawable.heart4))
                5 -> droidCardImage.setImageResource((R.drawable.heart5))
                6 -> droidCardImage.setImageResource((R.drawable.heart6))
                7 -> droidCardImage.setImageResource((R.drawable.heart7))
                8 -> droidCardImage.setImageResource((R.drawable.heart8))
                9 -> droidCardImage.setImageResource((R.drawable.heart9))
                10 -> droidCardImage.setImageResource((R.drawable.heart10))
                11 -> droidCardImage.setImageResource((R.drawable.heart11))
                12 -> droidCardImage.setImageResource((R.drawable.heart12))
                13 -> droidCardImage.setImageResource((R.drawable.heart13))
            }
            val set = AnimationSet(true)
            val scale = ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, 160 / 2f, 160 / 2f)

            set.fillAfter = true
            set.isFillEnabled = true
            set.addAnimation(scale)
            set.duration = 100
            set.interpolator = LinearInterpolator()
            droidCardImage.startAnimation(set)
        }, 100)
    }

    private fun showNextButton() {
        nextBtn.visibility = if (gameStart && (hitCount != 5 || loseCount != 5)) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun hideNextButton() {
        nextBtn.visibility = View.GONE
    }


    // resetButtonを表示
    private fun resetVNextButton() {
        resetButton.visibility = View.VISIBLE
    }

    // resetButtonを非表示
    private fun resetSNextButton() {
        resetButton.visibility = View.GONE
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

//        Toast.makeText(this, "「${item.title}」が押されました。", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun updateData() {
        val data = realm.where<Data>()
            .equalTo("id",1.toInt())
            .findFirst()
        realm.executeTransaction {
            var match = realm.where<Data>().max("match_high_low")
            match = (match?.toLong() ?: 0) + 1
            data?.match_high_low = match?.toInt()
        }

        Log.e("RealmUpdate", "更新しました:${realm.where<Data>().equalTo("id", 1.toInt()).findFirst()}")
    }

    private fun updateData_win() {
        val data = realm.where<Data>()
            .equalTo("id",1.toInt())
            .findFirst()
        realm.executeTransaction {
            var win = realm.where<Data>().max("win_high_low")
            var match = realm.where<Data>().max("match_high_low")
            win = (win?.toLong() ?: 0) + 1
            match = (match?.toLong() ?: 0) + 1
            data?.win_high_low = win?.toInt()
            data?.match_high_low = match?.toInt()

        }

        Log.e("RealmUpdate", "更新しました:${realm.where<Data>().equalTo("id", 1.toInt()).findFirst()}")
    }

}
