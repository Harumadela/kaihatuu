package com.example.kaihatu.game

import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.annotation.VisibleForTesting
import com.example.kaihatu.R
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

        // AIの操作中の場合は処理しない
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

        // AI
        if (currentPlayer == Stone.WHITE && ai !is AINone) {
            val aiCanPutPlaces = game.getAllCanPutPlaces(currentPlayer)
            if (aiCanPutPlaces.isEmpty()) {
                // AIが置ける場所がない場合、パス処理を行う
                onClickPass()
            } else {
                val choseByAI = ai.computeNext(game, currentPlayer)
                Handler(Looper.getMainLooper()).postDelayed({
                    // AIの操作が終了した後にターンを変更する
                    onClickPlace(choseByAI.x, choseByAI.y)
                }, 1000)
            }
        }
    }

    fun onClickPass() {
        val view = view ?: return

        // パスできる場所がなければ何もしない
        val canPutPlaces = game.getAllCanPutPlaces(currentPlayer)
        if (canPutPlaces.isNotEmpty()) {
            return
        }

        view.clearAllMarkPlaces()
        //view.showPassMessage()
        changePlayer()

        // AI
        if (currentPlayer == Stone.WHITE && ai !is AINone) {
            // AIの操作が始まったことをフラグで示す

            val choseByAI = ai.computeNext(game, currentPlayer)
            Handler(Looper.getMainLooper()).postDelayed({
                // AIの操作が終了した後にターンを変更する
                onClickPlace(choseByAI.x, choseByAI.y)
            }, 1000)

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