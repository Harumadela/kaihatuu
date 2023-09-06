package com.example.kaihatu

import kotlin.math.min

class Player(id:String) {
    private val id = id //プレイヤーID
    private val cards = mutableListOf<Card>() //手札
    private val max = 21 //許容される合計値の最大値

    fun getId():String{
        return id
    }

    //手札にカードを追加
    fun addCard(card: Card){
        cards.add(card)
    }

    fun removeAllCard(){
        cards.clear()
    }

    //合計値を取得
    fun getTotal():Int{
        //11以上のカードは10に合わせます
        return cards.map{min(10,it.getNum())}.sum()
    }

    //手札枚数を取得
    fun getCardCount():Int{
        return cards.size
    }

    //バーストしているかを取得
    fun isBurst():Boolean{
        return getTotal() > max
    }
}