package com.example.kaihatu

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.kaihatu.R
import com.example.kaihatu.databinding.ActivityMainBinding
import io.realm.Realm

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var realm: Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        realm = Realm.getDefaultInstance()

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
            R.id.action_user ->{
                var intent = Intent(this, Account::class.java)
                startActivity(intent)
            }
        }

//        Toast.makeText(this, "「${item.title}」が押されました。", Toast.LENGTH_SHORT).show()
        return true
    }



}