package com.example.test.game

import com.example.test.model.Place
import com.example.test.model.Stone


interface GameView {
    open fun putStone(place: Place)
    open fun setCurrentPlayerText(player: Stone)
    open fun showWinner(player: Stone, blackCount: Int, whiteCount: Int)
    open fun finishGame()
    open fun markCanPutPlaces(places: List<Place>)
    open fun clearAllMarkPlaces()
}