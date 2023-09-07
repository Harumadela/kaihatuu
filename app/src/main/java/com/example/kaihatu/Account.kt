package com.example.kaihatu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kaihatu.R
import com.example.kaihatu.databinding.AccountBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where


class Account : AppCompatActivity() {
    private lateinit var binding: AccountBinding
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AccountBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("アカウント")
        }

        realm = Realm.getDefaultInstance()

        var win_black = realm.where<Data>().max("win_black")
        var match_black = realm.where<Data>().max("match_black")
        var accuracy = realm.where<Data>().max("accuracy")
        var win_high_low = realm.where<Data>().max("win_high_low")
        var match_high_low = realm.where<Data>().max("match_high_low")
        var win_osero_black = realm.where<Data>().max("win_osero_black")
        var win_osero_white = realm.where<Data>().max("win_osero_white")
        var shooting_point = realm.where<Data>().max("shooting_point")



        binding.result1.text = match_black.toString() + "戦" + win_black.toString() + "勝"
        binding.result2.text = "正解率" + accuracy.toString() + "%"
        binding.result3.text = match_high_low.toString() + "戦" + win_high_low.toString() + "勝"
        binding.result4.text = "黒：" + win_osero_black.toString() + "勝　" + "白：" + win_osero_white.toString() + "勝"
        binding.result5.text = "  最高 " + shooting_point.toString() + "Pt"

        if(binding.result1.text == "null戦" + "null勝"){
//            register()
            binding.result1.text = "0戦" + "0勝"
        }
        if(binding.result2.text == "正解率null%"){
            binding.result2.text = "正解率0%"
        }
        if(binding.result3.text == "null戦" + "null勝"){
            binding.result3.text = "0戦" + "0勝"
        }
        if(binding.result4.text == "黒：null勝　" + "白：null勝"){
            binding.result4.text = "黒：0勝　" + "白：0勝"
        }
        if(binding.result4.text == "nullPt"){
            binding.result4.text = "  最高 0Pt"
        }
//        binding.register.setOnClickListener{register()}
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

    private fun register(){
        AlertDialog.Builder(this)
//                .setTitle("確認")
//                .setMessage("登録しますか？")
//                .setPositiveButton("OK") { dialog, which ->
                    realm.executeTransaction {
                        val target = realm.where<Data>().findAll()
                        target.deleteAllFromRealm()
                        val nextId1 = 1
                        val realmObject1 = realm.createObject<Data>(nextId1)
//                        realmObject1.name = binding.nameEdit.text.toString()
                        realmObject1.win_black = 0
                        realmObject1.match_black = 0
                        realmObject1.accuracy = 0
                        realmObject1.win_high_low = 0
                        realmObject1.match_high_low = 0
                        realmObject1.win_osero_black = 0
                        realmObject1.win_osero_white = 0
                        realmObject1.shooting_point = 0

                        Log.e("RealmInsert", "登録しました:${realm.where<Data>().findAll()}")
                    }
//                }
//                .setNegativeButton("Cancel") { dialog, which ->
//                    // Cancelの時は何もしない
//                }
//                .show()
    }

    private fun deleteData() {
        AlertDialog.Builder(this)
                .setTitle("確認")
                .setMessage("データを削除しますか？")
                .setPositiveButton("OK") { dialog, which ->
                    realm.executeTransaction {
                        val target = realm.where<Data>().findAll()
                        Log.e("RealmDelete", "テストです:${realm.where<Data>().findAll()}")
                        target.deleteAllFromRealm()
                        Log.e("RealmDelete", "テストです:${realm.where<Data>().findAll()}")
                    }
                    binding.result1.text = "0戦" + "0勝"
                    binding.result2.text = "正解率0%"
                    binding.result3.text = "0戦" + "0勝"
                    binding.result4.text = "黒：0勝　" + "白：0勝"
                    binding.result5.text = "  最高 0Pt"
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    // Cancelの時は何もしない
                }
                .show()

    }
}