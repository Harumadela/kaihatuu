package com.example.kaihatu.ai

import com.example.kaihatu.model.OseroGame
import com.example.kaihatu.model.Place
import com.example.kaihatu.model.Stone
import java.io.Serializable

interface OseroAI : Serializable {

    fun computeNext(game: OseroGame, color: Stone): Place
}
