package com.example.kaihatu.model.ai

import com.example.kaihatu.model.OseroGame
import com.example.kaihatu.model.Place
import com.example.kaihatu.model.Stone
import com.example.test.ai.OseroAI

class AIStrong : OseroAI {

    val boardRatings = arrayOf(
            arrayOf(30, -12, 0, -1, -1, 0, -12, 30),
            arrayOf(-12, -15, -3, -3, -3, -3, -15, -12),
            arrayOf(0, -3, 0, -1, -1, 0, -3, -1),
            arrayOf(-1, -3, -1, -1, -1, -1, -3, -1),
            arrayOf(-1, -3, -1, -1, -1, -1, -3, -1),
            arrayOf(0, -3, 0, -1, -1, 0, -3, -1),
            arrayOf(-12, -15, -3, -3, -3, -3, -15, -12),
            arrayOf(30, -12, 0, -1, -1, 0, -12, 30)
    )

    override fun computeNext(game: OseroGame, color: Stone): Place {
        return game.boardStatus.flatMap { it }
                .filter { game.canPut(Place(it.x, it.y, color)) }
                .maxBy { checkScore(it) + game.getCanChangePlaces(it).map { checkScore(it) }.sum() }!!
    }

    private fun checkScore(place: Place) = boardRatings[place.x][place.y]
}