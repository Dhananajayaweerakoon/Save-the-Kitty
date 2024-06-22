package com.example.savethekitty

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity(), Gametask {
    private lateinit var rootLayout: LinearLayout
    private lateinit var startbtn: Button
    private lateinit var mGameview: Gameview
    private lateinit var score: TextView
    private lateinit var highScoreText: TextView
    private lateinit var savekit: TextView
    private var highScore: Int = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("my_pref", Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("high_score", 0)

        startbtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        score = findViewById(R.id.score)
        highScoreText = findViewById(R.id.highScoreText)
        savekit = findViewById(R.id.savekit)
        mGameview = Gameview(this, this)

        startbtn.setOnClickListener {
            startGame()
        }
        updateHighScoreText()
    }

    private fun startGame() {
        mGameview = Gameview(this, this)
        mGameview.setBackgroundResource(R.drawable.back)
        rootLayout.addView(mGameview)
        startbtn.visibility = View.GONE
        score.visibility = View.GONE
        highScoreText.visibility = View.GONE
        savekit.visibility = View.GONE


        val previousScore = sharedPreferences.getInt("current_score", 0)
        score.text = "Score: $previousScore"
    }

    private fun updateHighScoreText() {
        highScoreText.text = "High Score: $highScore"
    }

    override fun closeGame(mScore: Int) {
        if (mScore > highScore) {
            highScore = mScore
            sharedPreferences.edit().putInt("high_score", highScore).apply()
            updateHighScoreText()
        }

        sharedPreferences.edit().putInt("current_score", mScore).apply()
        score.text = "Score: $mScore"
        rootLayout.removeView(mGameview)
        startbtn.visibility = View.VISIBLE
        score.visibility = View.VISIBLE
        highScoreText.visibility = View.VISIBLE
        savekit.visibility = View.VISIBLE
    }
}
