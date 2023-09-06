package com.example.test

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.test.databinding.BlackJackBinding
import io.realm.Realm
import io.realm.kotlin.where


class BlackJack : AppCompatActivity() {
    private lateinit var binding: BlackJackBinding
    private lateinit var realm: Realm
    private val cpu = Player("cpu")
    private val user = Player("user")
    private val deck = Deck()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BlackJackBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        realm = Realm.getDefaultInstance()

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("ブラックジャック")
        }

        binding.standBtn.setOnClickListener{stand()}
        binding.hitBtn.setOnClickListener{hit()}
        binding.resetBtn.setOnClickListener{reset()}

        // アクションバーに戻るを表示する
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        firstStep() //はじめの一歩

    }


    private fun run(){
        drawCard(cpu)//cpuがカードを引く

        Handler(Looper.getMainLooper()).postDelayed({
            if (cpu.getTotal() <= user.getTotal() && cpu.getTotal() < 21 && cpu.getCardCount() < 5) { //17未満且つカード枚数が５枚未満であればまた
                Handler(Looper.getMainLooper()).postDelayed({
                    run()
                }, 300)
            } else {
                Handler(Looper.getMainLooper()).removeCallbacks({}, this)//自信を削除
                Handler(Looper.getMainLooper()).postDelayed({
                    //勝敗を判定します
                    if (cpu.isBurst()) {
                        win()
                    } else if (cpu.getTotal() < user.getTotal()) {
                        win()
                    } else if (cpu.getTotal() > user.getTotal()) {
                        lose()
                    } else {
                        drow()
                    }
                }, 1000)
            }
        }, 1000)

    }


    private fun stand() {
        //はじめに伏せておいたカード画像を表示します
        //の前にボタンを無効化
        binding.standBtn.isEnabled = false
        binding.hitBtn.isEnabled = false

        binding.cpuCardImageView2.setImageResource(binding.cpuCardImageView2.tag.toString().toInt())
        val set = AnimationSet(true)
        val scale = ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, 100 / 2f, 100 / 2f)

        set.fillAfter = true
        set.isFillEnabled = true
        set.addAnimation(scale)
        set.duration = 100
        set.interpolator = LinearInterpolator()
        binding.cpuCardImageView2.startAnimation(set)
        binding.cpuTotalTextView.text = "${cpu.getTotal()}"

        //cpuは17未満の間カードを追加しなければなりません
        if (cpu.getTotal() <= user.getTotal() && cpu.getTotal() < 21) {
            Handler(Looper.getMainLooper()).postDelayed({
                run()
            }, 1000)
        } else{//17に達して入れば勝敗を判定します
            Handler(Looper.getMainLooper()).postDelayed({
                if (cpu.getTotal() < user.getTotal()) {
                    win()
                } else if (cpu.getTotal() > user.getTotal()) {
                    lose()
                } else {
                    drow()
                }
            }, 1000)
        }
    }

    private fun hit(){
        drawCard(user) //ヒットではユーザーがカードを引きます

        if(user.getCardCount() == 5) { //ImageViewを５つしか配置していないので制限します
            binding.hitBtn.isEnabled = false
        }

        if(user.isBurst()) { //カードを引いた後はバーストしているかを判定します
            Handler(Looper.getMainLooper()).postDelayed({
                lose() //負け表示
            }, 1000)
        }
    }

    private fun reset(){
        deck.initCards() //山札を初期化

        //カード画像をクリア
        for(i in 1..cpu.getCardCount()){
            val cardImageViewName = "cpuCardImageView$i"
            val cardImageViewId = resources.getIdentifier(cardImageViewName, "id", packageName)
            val cardImageView = findViewById<ImageView>(cardImageViewId)
            cardImageView.setImageDrawable(null) //画像をクリア
        }

        for(i in 1..user.getCardCount()){
            val cardImageViewName = "userCardImageView$i"
            val cardImageViewId = resources.getIdentifier(cardImageViewName, "id", packageName)
            val cardImageView = findViewById<ImageView>(cardImageViewId)
            cardImageView.setImageDrawable(null) //画像をクリア
        }

        //プレイヤーの手札をクリア
        cpu.removeAllCard()
        user.removeAllCard()

        //結果テキストなどをクリアしていきます
        binding.resultTextView.text = ""
        binding.cpuTotalTextView.text = "0"
        binding.userTotalTextView.text = "0"

        firstStep() //リセットボタンでもう一度ゲームを再開するようにします
    }
    //プレイヤーがカードを引く
    private fun drawCard(player: Player){
        val card = deck.getCard()//山札からカードを１枚引く
        player.addCard(card) //プレイヤーの手札にカードを追加する

        //こっからカード画像の表示と、合計値の更新をします
        //カード画像のリソースIDを取得
        val cardImageName = "${card.getMark()}%02d".format(card.getNum()) //Ex s02,h05,c10
        val cardImageId = resources.getIdentifier(cardImageName, "drawable", packageName)
        val cardImageId2 = resources.getIdentifier("ura2", "drawable", packageName)


        //CPUは２枚目のカードを伏せる処理
        if(player == cpu && player.getCardCount() == 2){
            binding.cpuCardImageView2.setImageResource(R.drawable.ura2) //裏面画像を表示
            binding.cpuCardImageView2.tag = "$cardImageId" //本来表示すべき画像のリソースIDを表示
        }
        else {
            //imageViewのリソースIDを取得
            val cardImageViewName = "${player.getId()}CardImageView${player.getCardCount()}" //Ex. cpuCardImageView3, userCardImageView5
            val cardImageViewId = resources.getIdentifier(cardImageViewName, "id", packageName)
            val cardImageView = findViewById<ImageView>(cardImageViewId)

            cardImageView.setImageResource(cardImageId2) //カード画像を設定
            var set = AnimationSet(true)
            var scale = ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f, 100 / 2f, 100 / 2f)

            set.fillAfter = true
            set.isFillEnabled = true
            set.addAnimation(scale)
            set.duration = 100
            set.interpolator = LinearInterpolator()
            if(player == user){
                if(player.getCardCount() == 1){
                    binding.userCardImageView1.startAnimation(set)
                }
                else if(player.getCardCount() == 2){
                    binding.userCardImageView2.startAnimation(set)
                }
                else if(player.getCardCount() == 3){
                    binding.userCardImageView3.startAnimation(set)
                }
                else if(player.getCardCount() == 4){
                    binding.userCardImageView4.startAnimation(set)
                }
                else if(player.getCardCount() == 5){
                    binding.userCardImageView5.startAnimation(set)
                }
            }
            else{
                if(cpu.getCardCount() == 1){
                    binding.cpuCardImageView1.startAnimation(set)
                }
                else if(cpu.getCardCount() == 2){
                    binding.cpuCardImageView2.startAnimation(set)
                }
                else if(cpu.getCardCount() == 3){
                    binding.cpuCardImageView3.startAnimation(set)
                }
                else if(cpu.getCardCount() == 4){
                    binding.cpuCardImageView4.startAnimation(set)
                }
                else if(cpu.getCardCount() == 5){
                    binding.cpuCardImageView5.startAnimation(set)
                }
            }


            Handler(Looper.getMainLooper()).postDelayed({
                cardImageView.setImageResource(cardImageId) //カード画像を設定
                val set = AnimationSet(true)
                val scale = ScaleAnimation(0.0f, 1.0f, 1.0f, 1.0f, 100 / 2f, 100 / 2f)

                set.fillAfter = true
                set.isFillEnabled = true
                set.addAnimation(scale)
                set.duration = 100
                set.interpolator = LinearInterpolator()
                if (player == user) {
                    if (player.getCardCount() == 1) {
                        binding.userCardImageView1.startAnimation(set)
                    } else if (player.getCardCount() == 2) {
                        binding.userCardImageView2.startAnimation(set)
                    } else if (player.getCardCount() == 3) {
                        binding.userCardImageView3.startAnimation(set)
                    } else if (player.getCardCount() == 4) {
                        binding.userCardImageView4.startAnimation(set)
                    } else if (player.getCardCount() == 5) {
                        binding.userCardImageView5.startAnimation(set)
                    }
                } else {
                    if (cpu.getCardCount() == 1) {
                        binding.cpuCardImageView1.startAnimation(set)
                    } else if (cpu.getCardCount() == 2) {
                        binding.cpuCardImageView2.startAnimation(set)
                    } else if (cpu.getCardCount() == 3) {
                        binding.cpuCardImageView3.startAnimation(set)
                    } else if (cpu.getCardCount() == 4) {
                        binding.cpuCardImageView4.startAnimation(set)
                    } else if (cpu.getCardCount() == 5) {
                        binding.cpuCardImageView5.startAnimation(set)
                    }
                }
            }, 100)
//            cardImageView.setImageResource(cardImageId) //カード画像を設定

            //TextViewのリソースIDを取得
            val totalTextViewName = "${player.getId()}TotalTextView"
            val totalTextViewId = resources.getIdentifier(totalTextViewName, "id", packageName)
            val totalTextView = findViewById<TextView>(totalTextViewId)
            totalTextView.text = "${player.getTotal()}" //合計を更新表示
        }
    }

    //    private val handler = Handler()
    private val delay = 3000L
    //はじめの一歩
    private fun firstStep(){
        //ここではゲーム開始時にプレイヤーに２枚カードを配布する
        //処理を書きます

        //ボタンを無効化
        binding.standBtn.isEnabled = false
        binding.hitBtn.isEnabled = false

        Handler(Looper.getMainLooper()).postDelayed({
            drawCard(cpu)
        }, 1000)
        Handler(Looper.getMainLooper()).postDelayed({
            drawCard(cpu)
        }, 2000)
        Handler(Looper.getMainLooper()).postDelayed({
            drawCard(user)

            //ボタンを有効化
            binding.standBtn.isEnabled = true
            binding.hitBtn.isEnabled = true
        }, 1000)
        Handler(Looper.getMainLooper()).postDelayed({
            drawCard(user)
        }, 2000)

    }
    //勝った場合の結果表示
    private fun win() {
        //ボタンを無効化
        binding.standBtn.isEnabled = false
        binding.hitBtn.isEnabled = false
        binding.resultTextView.setTextColor(Color.parseColor("#ffff00")) //黄色
        binding.resultTextView.text = getString(R.string.win)
        updateData_win()
    }

    //負けた場合の結果表示
    private fun lose() {
        //ボタンを無効化
        binding.standBtn.isEnabled = false
        binding.hitBtn.isEnabled = false
        binding.resultTextView.setTextColor(Color.parseColor("#ff0000")) //赤
        binding.resultTextView.text = getString(R.string.lose)
        updateData()
    }

    private fun drow(){
        //ボタンを無効化
        binding.standBtn.isEnabled = false
        binding.hitBtn.isEnabled = false
        binding.resultTextView.setTextColor(Color.parseColor("#0000ff")) //赤
        binding.resultTextView.text = getString(R.string.drow)
        updateData()
    }

    private fun updateData() {
        val data = realm.where<Data>()
                .equalTo("id", 1.toInt())
                .findFirst()
        realm.executeTransaction {
            var win = realm.where<Data>().max("win_black")
            var match = realm.where<Data>().max("match_black")
            match = (match?.toLong() ?: 0) + 1
            data?.match_black = match?.toInt()
        }

        Log.e("RealmUpdate", "更新しました:${realm.where<Data>().equalTo("id", 1.toInt()).findFirst()}")
    }

    private fun updateData_win() {
        val data = realm.where<Data>()
                .equalTo("id", 1.toInt())
                .findFirst()
        realm.executeTransaction {
            var win = realm.where<Data>().max("win_black")
            var match = realm.where<Data>().max("match_black")
            win = (win?.toLong() ?: 0) + 1
            match = (match?.toLong() ?: 0) + 1
            data?.win_black = win?.toInt()
            data?.match_black = match?.toInt()

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
            R.id.action_user -> {
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