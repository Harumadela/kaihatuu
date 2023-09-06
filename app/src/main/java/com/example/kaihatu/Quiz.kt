package com.example.kaihatu

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kaihatu.R
import com.example.kaihatu.databinding.QuizBinding
import com.example.kaihatu.ResultActivity

class Quiz : AppCompatActivity() {

    private lateinit var binding: QuizBinding
    private  lateinit var timer: CountDownTimer
    private var rightAnswer: String? = null
    private var rightAnswerCount = 0
    private var quizCount = 1
    private val QUIZ_COUNT = 10
    val second:Long = 11;
    var secondAll: Long = second;



    private val quizData = mutableListOf(
            mutableListOf("点滴の成分は何と同じ？", "ポカリスエット", "血液", "砂糖水", "オレンジジュース"),
            mutableListOf("ハイジャックの語源は？", "ジャックという名前が多かったから", "空高い所で襲撃するから", "テンションがハイだから", "高い金銭を要求するから"),
            mutableListOf("タイの首都名が長い理由は？", "詩が首都名になったから", "首都名の候補を全て繋げたから", "政治家の名前を全て繋げたから", "神様の名前を繋げたから"),
            mutableListOf("トランプは海外で何と呼ばれている？", "プレイングカード", "54カード", "トランプカード", "ギャンブルカード"),
            mutableListOf("考える人は何を考えている？", "考えているわけではない", "恋人について", "自分の将来", "地球温暖化について"),
            mutableListOf("血液型の種類がABC型ではなくABO型の理由は？", "勘違いでそうなった", "血液型を見つけた人の頭文字", "血液型の英語名の頭文字", "Cが嫌いだったから"),
            mutableListOf("孫の手の「孫」とは誰の事？", "美女", "孫", "親", "子供"),
            mutableListOf("英語圏で＠は何と呼ばれる？", "カタツムリ", "ロールケーキ", "アルマジロ", "クッキー"),
            mutableListOf("海外で＠を意味しない言葉はどれ？", "ロールケーキ", "蛆虫", "犬", "アヒル"),
            mutableListOf("オセロのコマのもとになったものは何？", "牛乳瓶の蓋", "ボタン", "500円玉", "ゲームセンターのコイン"),
            mutableListOf("「チャック」「ファスナー」「ジッパー」の違いは何？", "名称が違うだけで違いはない", "つなぎ目の数が違う", "使用される用途が違う", "長さが違う"),
            mutableListOf("ガリガリ君の当たりの確率は何％？", "3％", "1％", "2％", "4％"),
            mutableListOf("昆布が海の中でダシを出さないのは何故？", "昆布が生きていくために必要な栄養素だから", "実はダシは出ている", "乾燥させる段階でダシが出るから", "海水とお湯では浸透圧が違うから"),
            mutableListOf("月の土地は1200坪で何円？", "2700円", "270000円", "540000円", "1080000円"),
            mutableListOf("通話中に電話から聞こえる声は誰の声？", "声が似ている別人", "機械音", "通話相手", "自分"),
            mutableListOf("トランプの神経衰弱の名前の由来は？", "カードを記憶するのに神経を使うから", "英語を日本語に直訳したから", "昔は拷問として使われていたから", "プレイした人の神経が衰弱したデータがあるから"),
            mutableListOf("カルピスの水玉模様の由来は？", "天の川", "泡", "炭酸", "雨"),
            mutableListOf("Googleのロゴの由来は？", "レゴブロック", "野菜", "ハンバーガー", "信号機"),
            mutableListOf("ダイヤモンドより硬い鉱石はどれ？", "ロンズデーライト", "ルベライトトルマリン", "ヘリオドール", "パロットクリソベリル"),
            mutableListOf("地球で最も硬い鉱石はどれ？", "ウルツァイト窒化ホウ素", "ロンズデーライト", "炭化ジルコニウム", "二ホウ化チタン"),
            mutableListOf("選択肢の中で肉の焼き方で最も生に近いのはどれ？", "ブルー", "レア", "ウェル", "ブルーレア"),
            mutableListOf("みかんの白い筋の正式名称は？", "アルベド", "ペプチド", "フラベド", "リコピン"),
            mutableListOf("枝豆は何類に分類されている？", "野菜類", "豆類", "肉類", "果物類"),
            mutableListOf("ウインナーとソーセージは何が違う？", "違わない(ウインナーはソーセージの一種)", "違わない(ソーセージはウインナーの一種)", "長さ", "太さ"),
            mutableListOf("選択肢の中でパスタの一種ではないものはどれ？", "アモーレ", "スパゲティ", "カッペリーニ", "ラビオリ"),
            mutableListOf("バターとマーガリンの違いは何？", "動物性脂肪と植物性脂肪の違い", "牛のミルクか山羊のミルクかの違い", "生まれた国の違い", "名称が違うだけで違いはない"),
            mutableListOf("チャーハンと焼き飯の違いは何？", "卵を入れる順番", "使用する油の違い", "愚材の違い", "名称が違うだけで違いはない"),
            mutableListOf("「そうめん」「ひやむぎ」「うどん」「きしめん」の違いは何？", "太さ", "長さ", "重さ", "味"),
            mutableListOf("ピーマンとパプリカは何が違う？", "果肉の厚さ", "色", "味", "名称が違うだけで違いはない"),
            mutableListOf("食べ物を意味するバイキングは何語？", "日本語", "英語", "フランス語", "イタリア語"),
            mutableListOf("蜂蜜は腐らないが、保存方法として間違っているものはどれ？", "冷蔵庫で保存する", "涼しいところで保存する", "湿気が少ないところで保存する", "直射日光を避けて保存する"),
            mutableListOf("継ぎ足しのタレが腐らない理由として、間違っているものはどれ？", "かき混ぜる事により菌が繁殖しにくくなるから", "焼いた具材を入れることで低温殺菌されるから", "タレの塩分と糖分の濃度が高いから", "継ぎ足す度に新しいタレの割合が増えていくから"),
            mutableListOf("ポテトチップスが誕生したきっかけはどれ？", "シェフの嫌がらせ", "他店舗との差別化", "一つの芋から満足感を得るため", "味を誤魔化すため"),
            mutableListOf("以下の市販かき氷シロップの中で、違う味のものはどれ？", "抹茶", "ブルーハワイ", "イチゴ", "メロン"),
            mutableListOf("ビタミンCの量を表す基準として「レモン◯個分」と表記されるのは何故？", "1日に必要なビタミンCを含んでいるから", "果物の中で一番ビタミンCが多いから", "ビタミンCを実は余り含んでおらず、大袈裟に宣伝するため", "普段そのまま食べることがないから"),
            mutableListOf("そこのお前！レモン一個に含まれるビタミンCはレモン◯個分だぜ！", "5個", "10~12個", "1個", "0.5個"),
            mutableListOf("グレープフルーツの名前の由来は？", "ブドウのように実が生るから", "味がブドウに似ているから", "実の色がブドウに似ているから", "昔はブドウの分類が野菜だったから"),
            mutableListOf("シュークリームの「シュー」の意味は？", "キャベツ", "岩", "美味しい", "膨らんだ"),
            mutableListOf("「ティラミス」の意味は？", "私を元気にして欲しい", "美味しいもの", "層", "積み重なる"),
            mutableListOf("レトルト食品のレトルトの意味は？", "加圧加熱殺菌された", "長持ちする", "真空となった", "安くて美味しい"),
            mutableListOf("すき焼きに卵をつける本来の理由は？", "火傷を防ぐため", "肉を柔らかくするため", "牛肉を殺菌するため", "味をマイルドにするため"),
            mutableListOf("ホットドッグの名前の由来は？", "ソーセージが犬のダックスフントに似ているから", "ソーセージの色が犬の色に似ているから", "犬の食べ物として考案されたから", "昔は食用の犬の肉が使われていたから")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = QuizBinding.inflate(layoutInflater).apply { setContentView(this.root) }

        // アクションバーに戻るを表示する
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val actionBar: ActionBar? = supportActionBar
        if (actionBar != null) {
            actionBar.setTitle("クイズ")
        }

        binding.timeTextView.text = "${second.toString().padStart(2, '0')}"

        // binding.stopButton.isVisible = false

        timer = object : CountDownTimer(secondAll * 1000, 1000) {
            override fun onFinish() {
                timeOut()
                secondAll = second;
                binding.timeTextView.text = "${second.toString().padStart(2, '0')}"
            }

            override fun onTick(millisUntilFinished: Long) {
                secondAll--
                binding.timeTextView.text = "${((secondAll % 60).toString().padStart(2, '0'))}"
            }
        }

        binding = QuizBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        quizData.shuffle()

        showNextQuiz()
    }

    fun showNextQuiz() {

        binding.timeTextView.text = "${second.toString().padStart(2, '0')}"
        //secondAll = hour * 3600 + minute * 60 + second
        secondAll = second;

        // カウントラベルの更新
        binding.countLabel.text = getString(R.string.count_label, quizCount)

        // クイズを１問取り出す
        val quiz = quizData[0]

        // 問題をセット
        binding.questionLabel.text = quiz[0]

        // 正解をセット
        rightAnswer = quiz[1]

        // 都道府県名を削除
        quiz.removeAt(0)

        // 正解と選択肢３つをシャッフル
        quiz.shuffle()

        // 正解と選択肢をセット
        binding.answerBtn1.text = quiz[0]
        binding.answerBtn2.text = quiz[1]
        binding.answerBtn3.text = quiz[2]
        binding.answerBtn4.text = quiz[3]

        // 出題したクイズを削除する
        quizData.removeAt(0)

        timer.start()
    }

    fun checkAnswer(view: View) {
        // どの解答ボタンが押されたか
        val answerBtn: Button = findViewById(view.id)
        val btnText = answerBtn.text.toString()
        timer.cancel()

        // ダイアログのタイトルを作成
        val alertTitle: String
        if (btnText == rightAnswer) {
            alertTitle = "正解!"
            rightAnswerCount++
        } else {
            alertTitle = "不正解..."
        }

        // ダイアログを作成
        AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage("答え : $rightAnswer")
                .setPositiveButton("OK") { dialogInterface, i ->
                    checkQuizCount()
                }
                .setCancelable(false)
                .show()
    }

    fun checkQuizCount() {
        if (quizCount == QUIZ_COUNT) {
            // 結果画面を表示
            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("RIGHT_ANSWER_COUNT", rightAnswerCount)
            startActivity(intent)

        } else {
            quizCount++
            showNextQuiz()
        }
    }

    fun timeOut() {
        timer.cancel()

        val alertTitle: String
        alertTitle = "不正解..."

        // ダイアログを作成
        AlertDialog.Builder(this)
                .setTitle(alertTitle)
                .setMessage("答え : $rightAnswer")
                .setPositiveButton("OK") { dialogInterface, i ->
                    checkQuizCount()
                }
                .setCancelable(false)
                .show()
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
}