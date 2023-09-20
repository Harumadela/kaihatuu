package com.example.kaihatu.game

import android.os.Handler
import android.os.Looper
import androidx.annotation.VisibleForTesting
import com.example.kaihatu.ai.OseroAI
import com.example.kaihatu.model.OseroGame
import com.example.kaihatu.model.Place
import com.example.kaihatu.model.Stone
import com.example.kaihatu.model.ai.AINone



class GamePresenter {

    private val game: OseroGame = OseroGame()
    private lateinit var ai: OseroAI
    private var view: GameView? = null
    val boardSize = game.BOARD_SIZE
    private var isPassTurn = false


    /** 現在どちらのターンか **/
    var currentPlayer = Stone.BLACK


    fun onCreate(view: GameView, ai: OseroAI = AINone()) {
        this.view = view
        this.ai = ai
        view.setCurrentPlayerText(Stone.BLACK)
        game.getInitialPlaces().forEach { putStone(it) }
        view.markCanPutPlaces(game.getAllCanPutPlaces(currentPlayer))
    }

    fun onClickPlace(x: Int, y: Int) {
        val view = view ?: return
        val clickPlace = Place(x, y, currentPlayer)
        if (!game.canPut(clickPlace)) {
            return
        }
        view.clearAllMarkPlaces()
        putStone(clickPlace)
        game.getCanChangePlaces(clickPlace).forEach { putStone(it) }

        // 終了処理
        if (game.isGameOver()) {
            val blackCount = game.countStones(Stone.BLACK)
            val whiteCount = game.countStones(Stone.WHITE)
            view.showWinner(if (blackCount > whiteCount) Stone.BLACK else Stone.WHITE, blackCount, whiteCount)
            view.finishGame()
        }
        // ターン切替
        changePlayer()
        view.markCanPutPlaces(game.getAllCanPutPlaces(currentPlayer))

        // パス
        if (game.getAllCanPutPlaces(currentPlayer).isEmpty()) {
            if (isPassTurn) {
                // 連続してパスが続いた場合、ゲーム終了とする
                view.clearAllMarkPlaces()
                //view.showPassMessage()
                view.finishGame()
            } else {
                // パス
                isPassTurn = true
                view.clearAllMarkPlaces()
                //view.showPassMessage()
                changePlayer()
            }
        } else {
            // パスではない場合、置けるマスを表示
            isPassTurn = false
            view.markCanPutPlaces(game.getAllCanPutPlaces(currentPlayer))
        }

        // AI
        if (ai !is AINone && currentPlayer == Stone.WHITE) {
            val choseByAI = ai.computeNext(game, currentPlayer)
            Handler(Looper.getMainLooper()).postDelayed({
                onClickPlace(choseByAI.x, choseByAI.y)
            }, 1000)
        }
    }

    fun onClickPass() {
        val view = view ?: return
        view.clearAllMarkPlaces()
        //view.showPassMessage()
        changePlayer()

        // AI
        if (ai !is AINone && currentPlayer == Stone.WHITE) {
            val choseByAI = ai.computeNext(game, currentPlayer)
            onClickPlace(choseByAI.x, choseByAI.y)
        }
    }

    /** ターンを変更する */
    @VisibleForTesting
    fun changePlayer() {
        if (isPassTurn) {
            isPassTurn = false
            return
        }
        currentPlayer = currentPlayer.other()
        view?.setCurrentPlayerText(currentPlayer)
    }

    /** 指定した位置に石を置く。ターンは切り替わらない */
    @VisibleForTesting
    fun putStone(place: Place) {
        game.boardStatus[place.x][place.y].stone = place.stone
        view?.putStone(place)

    }
}