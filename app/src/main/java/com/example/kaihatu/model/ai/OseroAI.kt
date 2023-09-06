package com.example.test.ai

import com.example.test.model.OseroGame
import com.example.test.model.Place
import com.example.test.model.Stone
import java.io.Serializable

interface OseroAI : Serializable {

    fun computeNext(game: OseroGame, color: Stone): Place
}
