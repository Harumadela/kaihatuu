package com.example.test.model.ai

import com.example.test.ai.OseroAI
import com.example.test.model.OseroGame
import com.example.test.model.Place
import com.example.test.model.Stone

class AIWeak : OseroAI {
    override fun computeNext(game: OseroGame, color: Stone): Place {
        return game.boardStatus.flatMap { it }
                .filter { game.canPut(Place(it.x, it.y, color)) }
                .maxBy { game.getCanChangePlaces(it).count() }!!
    }
}