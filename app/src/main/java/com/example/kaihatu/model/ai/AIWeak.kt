package com.example.kaihatu.model.ai

import com.example.kaihatu.ai.OseroAI
import com.example.kaihatu.model.OseroGame
import com.example.kaihatu.model.Place
import com.example.kaihatu.model.Stone

class AIWeak : OseroAI {
    override fun computeNext(game: OseroGame, color: Stone): Place {
        return game.boardStatus.flatMap { it }
                .filter { game.canPut(Place(it.x, it.y, color)) }
                .maxBy { game.getCanChangePlaces(it).count() }!!
    }
}