package com.example.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.ActivityResultBinding
import io.realm.Realm
import io.realm.kotlin.where


class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_result)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        realm = Realm.getDefaultInstance()

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("ミニゲーム")
        }

        // 正解数を取得
        val score = intent.getIntExtra("RIGHT_ANSWER_COUNT", 0)

        // TextViewに表示する
        binding.resultLabel.text = getString(R.string.result_score, score)

        val data = realm.where<Data>()
                .equalTo("id",1.toInt())
                .findFirst()
        realm.executeTransaction {
            var accuracy = realm.where<Data>().max("accuracy")
            accuracy = score *10
            data?.accuracy = accuracy?.toInt()
            Log.e("RealmUpdate", "スコア:${accuracy}")
        }

        Log.e("RealmUpdate", "更新しました:${realm.where<Data>().equalTo("id", 1.toInt()).findFirst()}")


        // 「もう一度」ボタン
        binding.tryAgainBtn.setOnClickListener {
            startActivity(Intent(this, Quiz::class.java))
        }

        binding.backHome.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
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

//        Toast.makeText(this, "「${item.title}」が押されました。", Toast.LENGTH_SHORT).show()
        return true
    }
}