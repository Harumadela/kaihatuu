package com.example.kaihatu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kaihatu.databinding.ActivityMainBinding
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        realm = Realm.getDefaultInstance()

        register()

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("ミニゲーム")
        }

        val tag = "ListFragment"
        var fragment = supportFragmentManager.findFragmentByTag(tag)
        if(fragment == null){
            fragment = ListFragment()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.content, fragment, tag)
            }.commit()
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
            R.id.action_user -> {
                var intent = Intent(this, Account::class.java)
                startActivity(intent)
            }
        }

//        Toast.makeText(this, "「${item.title}」が押されました。", Toast.LENGTH_SHORT).show()
        return true
    }

    private fun register(){
        val data = realm.where<Data>()
            .equalTo("id", 1.toInt())
            .findFirst()
        if(data == null){
            realm.executeTransaction {
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
                realmObject1.win_osero_black = 0
                realmObject1.win_osero_white = 0
                realmObject1.shooting_point = 0

                Log.e("RealmInsert", "登録しました:${realm.where<Data>().findAll()}")
            }
        }
    }



}