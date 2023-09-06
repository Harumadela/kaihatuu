package com.example.test

import com.example.test.com.example.test.Card

class Deck {
    private val cards = mutableListOf<Card>() //山札
    private var cardIndex = -1

    //山札を初期化
    fun initCards(){
        cards.clear() //山札をクリア

        for(i in 1..13){ //52枚のカードを山札に追加
            cards.add(Card(i,'s'))//♠
            cards.add(Card(i,'h'))//♥
            cards.add(Card(i,'d'))//♦
            cards.add(Card(i,'c'))//☘
        }
        cards.shuffle() //山札をシャッフル

        cardIndex = 0 //カードを引く位置を先頭に
    }

    init{
        initCards()
    }

    //カードを一枚取得
    fun getCard():Card{
        return cards[cardIndex++]
    }
}