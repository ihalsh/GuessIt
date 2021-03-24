package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class ScoreViewModel(finalScore: Int) : ViewModel() {

    val score = finalScore
    private val _isPlayAgain = MutableLiveData<Boolean>()
    val isPlayAgain: LiveData<Boolean>
        get() = _isPlayAgain

    init {
        Timber.i("Final score is: $finalScore")
        _isPlayAgain.value = false
    }

    fun onPlayAgain() {
        _isPlayAgain.value = true
    }

    fun playAgainActionFinished() {
        _isPlayAgain.value = false
    }

}