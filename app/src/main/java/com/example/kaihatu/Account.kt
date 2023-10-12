package com.example.kaihatu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.kaihatu.databinding.AccountBinding
import com.example.kaihatu.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.recycler_item.*


class Account : AppCompatActivity() {
    private lateinit var binding: AccountBinding
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("アカウント")
        }

        realm = Realm.getDefaultInstance()

        var win_black = realm.where<Data>().max("win_black")
        var match_black = realm.where<Data>().max("match_black")
        var total_answer = realm.where<Data>().max("total_answer")
        var total_current = realm.where<Data>().max("total_current")
        var win_high_low = realm.where<Data>().max("win_high_low")
        var match_high_low = realm.where<Data>().max("match_high_low")
        var win_osero = realm.where<Data>().max("win_osero")
        var match_osero = realm.where<Data>().max("match_osero")
        var shooting_point = realm.where<Data>().max("shooting_point")

        var black_rate = (win_black?.toLong() ?: 0)?.toDouble() / (match_black?.toLong() ?: 0)?.toDouble() * 100
        var quiz_accuracy =  (total_current?.toLong() ?: 0)?.toDouble() / (total_answer?.toLong() ?: 0)?.toDouble() * 100
        var high_rate = (win_high_low?.toLong() ?: 0)?.toDouble() / (match_high_low?.toLong() ?: 0)?.toDouble() * 100
        var osero_rate = (win_osero?.toLong() ?: 0)?.toDouble() / (match_osero?.toLong() ?: 0)?.toDouble() * 100

        black_rate = Math.round(black_rate * 10.0) / 10.0
        quiz_accuracy= Math.round(quiz_accuracy * 10.0) / 10.0
        high_rate = Math.round(high_rate * 10.0) / 10.0
        osero_rate = Math.round(osero_rate * 10.0) / 10.0

        if(black_rate.isNaN()){
            black_rate = 0.0
        }
        if(quiz_accuracy.isNaN()){
            quiz_accuracy = 0.0
        }
        if(high_rate.isNaN()){
            high_rate = 0.0
        }
        if(osero_rate.isNaN()){
            osero_rate = 0.0
        }


        val list: MutableList<MyItem> = mutableListOf()

        for (i in 0..5) {
            when(i){
                0 -> list.add(MyItem(i, "BlackJack", " $match_black 戦　 $win_black 勝" +
                        "　　　　　　勝率： $black_rate %"))
                1 -> list.add(MyItem(i, "クイズ", "　$total_answer 問中： $total_current 正解" +
                                "　　　　　　正解率： $quiz_accuracy %"))
                2 -> list.add(MyItem(i, "High＆Low", "　$match_high_low 戦　 $win_high_low 勝" +
                        "　　　　　　勝率： $high_rate %"))
                3 -> list.add(MyItem(i, "オセロ", "　$match_osero 戦　 $win_osero 勝" +
                        "　　　　　　勝率： $osero_rate %"))
                4 -> list.add(MyItem(i, " 　シューティング", "　　最高　$shooting_point pt"))
            }

        }
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val myAdapter = MyAdapter()
        recyclerView.adapter = myAdapter

        myAdapter.submitList(list)

        binding.delete.setOnClickListener{deleteData()}
        Log.e("RealmInsert", "初期化:${realm.where<Data>().findAll()}")

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
            R.id.action_user -> {
                var intent = Intent(this, Account::class.java)
                startActivity(intent)
            }
        }

        return true
    }


    private fun deleteData() {
        AlertDialog.Builder(this)
                .setTitle("確認")
                .setMessage("データを削除しますか？")
                .setPositiveButton("OK") { dialog, which ->
                    realm.executeTransaction {
                        val target = realm.where<Data>().findAll()

                        target.deleteAllFromRealm()
                        Log.e("RealmDelete", "削除テストです:${realm.where<Data>().findAll()}")
                        register()
                        var intent = Intent(this, Account::class.java)
                        startActivity(intent)
                    }
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    // Cancelの時は何もしない
                }
                .show()
    }

    private fun register(){
        val data = realm.where<Data>()
            .equalTo("id", 1.toInt())
            .findFirst()
        if(data == null){
            val target = realm.where<Data>().findAll()
            target.deleteAllFromRealm()
            val nextId1 = 1
            val realmObject1 = realm.createObject<Data>(nextId1)
            realmObject1.win_black = 0
            realmObject1.match_black = 0
            realmObject1.total_answer = 0
            realmObject1.total_current = 0
            realmObject1.win_high_low = 0
            realmObject1.match_high_low = 0
            realmObject1.win_osero = 0
            realmObject1.match_osero = 0
            realmObject1.shooting_point = 0

            Log.e("RealmInsert", "登録しました:${realm.where<Data>().findAll()}")
        }
    }
}

