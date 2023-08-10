package com.pomodorotechniqueapp

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.pomodorotechniqueapp.extensions.showToast

class MainActivity : AppCompatActivity() {
    private val START_TIME_IN_MILLIS: Long = 25 * 60 * 1000
    private var remainingTime: Long = START_TIME_IN_MILLIS
    private lateinit var startButton: Button
    private lateinit var resetTimer: TextView
    private lateinit var timer: TextView
    private lateinit var title: TextView
    private lateinit var myTimer: CountDownTimer
    private lateinit var progressBar: ProgressBar
    private var isTimeRunning = false
    private var isPaused = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeViews()

        startButton.setOnClickListener {
            if (!isTimeRunning) {
                title.setText(R.string.keep_going)
                startTimer()
                isTimeRunning = true
                startButton.text = getString(R.string.pause)
            } else if (isPaused) {
                title.setText(R.string.keep_going)
                resumeTimer(remainingTime)
                isTimeRunning = true
                isPaused = false
                startButton.text = getString(R.string.pause)
            } else {
                title.setText(R.string.take_pomodoro)
                pauseTimer()
                isPaused = true
                startButton.text = getString(R.string.resume)
            }
        }
        resetTimer.setOnClickListener { resetTime() }
    }

    private fun resumeTimer(remainingTime: Long) {
        startTimer(remainingTime)
    }

    private fun pauseTimer() {
        myTimer.cancel()
        updateTimer()
    }

    private fun initializeViews() {
        startButton = findViewById(R.id.start_button)
        resetTimer = findViewById(R.id.reset)
        timer = findViewById(R.id.timer)
        title = findViewById(R.id.pomodoro_title)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun startTimer(time: Long = START_TIME_IN_MILLIS) {
        myTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(timeLeftInMillis: Long) {
                remainingTime = timeLeftInMillis
                updateTimer()
                progressBar.progress = calculateProgressBar()
            }

            private fun calculateProgressBar() =
                (remainingTime.toDouble() / START_TIME_IN_MILLIS.toDouble() * 100).toInt()

            override fun onFinish() {
                showToast("take 5 minutes break")
                resetTime()
            }
        }.start()
    }

    private fun resetTime() {
        myTimer.cancel()
        timer.setText(R.string.default_time)
        title.setText(R.string.take_pomodoro)
        isTimeRunning = false
        progressBar.progress = 100

    }

    private fun updateTimer() {
        //convert milli seconds to minutes // minutes = millisecond / 1000 / 60
        val minutes = remainingTime.div(1000).div(60)
        //convert milli seconds to seconds // seconds = millisecond / 1000 % 60
        val seconds = remainingTime.div(1000) % 60

        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        timer.text = formattedTime
    }
}
