package com.example.kaihatu.model.ai

import com.example.kaihatu.ai.OseroAI
import com.example.kaihatu.model.OseroGame
import com.example.kaihatu.model.Place
import com.example.kaihatu.model.Stone

/**
 * AIを使用しない場合
 */
class AINone : OseroAI {

    override fun computeNext(game: OseroGame, color: Stone): Place {
        throw IllegalAccessException()
    }

}
