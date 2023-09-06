package com.example.kaihatu

class Card(num:Int, mark:Char) {
    private val num = num //カード番号
    private val mark = mark //種類

    fun getNum():Int{
        return num
    }

    fun getMark():Char{
        return mark
    }
}