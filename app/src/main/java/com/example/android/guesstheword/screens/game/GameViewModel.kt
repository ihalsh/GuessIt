/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.screens.game.GameViewModel.Companion.COUNTDOWN_TIME
import timber.log.Timber

class GameViewModel : ViewModel() {

    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _timeLeft = MutableLiveData<String>()
    val timeLeft: LiveData<String>
        get() = _timeLeft

    // The game finished event
    private val _isGameFinished = MutableLiveData<Boolean>()

    val isGameFinished: LiveData<Boolean>
        get() = _isGameFinished

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val timer: CountDownTimer by lazy {
        object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = DateUtils.formatElapsedTime(millisUntilFinished / 1000)
                Timber.i("Time left: ${timeLeft.value}")
            }

            override fun onFinish() {
                _isGameFinished.value = true
            }
        }
    }


    init {
        Timber.i("ViewModel created.")
        _score.value = 0
        _word.value = ""
        _timeLeft.value = ""
        _isGameFinished.value = false
        resetList()
        nextWord()
        timer.start()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    fun gameFinishEventEnded() {
        _isGameFinished.value = false
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = _score.value?.minus(1)?.coerceIn(0, Int.MAX_VALUE)
        nextWord()
    }

    fun onCorrect() {
        _score.value = _score.value?.plus(1)
        nextWord()
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        Timber.i("onCleared called.")
    }

    companion object {
        // These represent different important times
        // This is when the game is over
        const val DONE = 0L

        // This is the number of milliseconds in a second
        const val ONE_SECOND = 1_000L

        // This is the total time of the game
        const val COUNTDOWN_TIME = 5_000L
    }
}