/***************************************************************************************
 *  REFERENCES
 *  Title: Konfetti
 *  Author: Dion Segijn
 *  License: ISC license
 *  Last Update: Feb 20, 2022
 *  URL: https://github.com/DanielMartinus/Konfetti
 ***************************************************************************************/

package com.google.wordle

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.wordle.FourLetterWordList.getRandomFourLetterWord
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextWord = findViewById<EditText>(R.id.editTextGuess)
        val guessButton = findViewById<Button>(R.id.guessButton)
        val revealGuess = findViewById<TextView>(R.id.correctGuess)

        val guessList0 = listOf(
            findViewById<TextView>(R.id.letter00),
            findViewById(R.id.letter01),
            findViewById(R.id.letter02),
            findViewById(R.id.letter03)
        )

        val guessList1 = listOf(
            findViewById<TextView>(R.id.letter10),
            findViewById(R.id.letter11),
            findViewById(R.id.letter12),
            findViewById(R.id.letter13)
        )

        val guessList2 = listOf(
            findViewById<TextView>(R.id.letter20),
            findViewById(R.id.letter21),
            findViewById(R.id.letter22),
            findViewById(R.id.letter23)
        )

        val numberOfGuesses = 3
        val numberOfLetters = 4
        val target = getRandomFourLetterWord()
        var counter = 0
        guessButton.isEnabled = false

        editTextWord.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                guessButton.isEnabled = s.toString().trim().length == 4
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        guessButton.setOnClickListener {
            hideKeyboard()
            val guess = editTextWord.text.toString().uppercase()
            when (counter) {
                0 -> {
                    for (column in 0 until numberOfLetters) {
                        guessList0[column].text = guess[column].toString()
                        guessList0[column].setBackgroundColor(letterColor(guess[column], target, column))
                    }
                    checkGuess(guess, target, editTextWord, guessButton)
                }
                1 -> {
                    for (column in 0 until numberOfLetters) {
                        guessList1[column].text = guess[column].toString()
                        guessList1[column].setBackgroundColor(letterColor(guess[column], target, column))
                    }
                    checkGuess(guess, target, editTextWord, guessButton)
                }
                2 -> {
                    for (column in 0 until numberOfLetters) {
                        guessList2[column].text = guess[column].toString()
                        guessList2[column].setBackgroundColor(letterColor(guess[column], target, column))
                    }
                    checkGuess(guess, target, editTextWord, guessButton)
                }
            }
            counter++
            editTextWord.text.clear()
            if (counter == numberOfGuesses) {
                guessButton.visibility = View.INVISIBLE
                editTextWord.visibility = View.INVISIBLE
                revealGuess.visibility = View.VISIBLE
                revealGuess.text = target
            }
        }

    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    private fun letterColor(currentGuess: Char, target: String, currentColumn: Int): Int {
        return if (currentGuess in target) {
            if (currentGuess == target[currentColumn]) Color.GREEN else Color.YELLOW
        } else Color.GRAY
    }

    private fun checkGuess(guess: String, target: String, editText: EditText, button: Button) {
        if (guess == target) {
            editText.visibility = View.INVISIBLE
            button.visibility = View.INVISIBLE
            val viewWin: KonfettiView = findViewById(R.id.konfettiView)
            val party = Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100),
                position = Position.Relative(0.5, 0.3)
            )
            viewWin.start(party)
            val revealGuess = findViewById<TextView>(R.id.correctGuess)
            revealGuess.visibility = View.VISIBLE
            revealGuess.text = target
        }
    }
}