package com.example.test.model.ai

import com.example.test.ai.OseroAI
import com.example.test.model.OseroGame
import com.example.test.model.Place
import com.example.test.model.Stone

/**
 * AIを使用しない場合
 */
class AINone : OseroAI {

    override fun computeNext(game: OseroGame, color: Stone): Place {
        throw IllegalAccessException()
    }

}
