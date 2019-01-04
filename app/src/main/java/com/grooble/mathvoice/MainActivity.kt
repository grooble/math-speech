package com.grooble.mathvoice

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.toast
import kotlin.random.*
import android.speech.RecognizerIntent
import android.content.Intent
import com.grooble.mathvoice.NumberParser


class MainActivity : AppCompatActivity() {

    private lateinit var submitButton: Button
    private lateinit var problemView : TextView
    private lateinit var answerView: TextView
    private val REQUEST_SPEECH_RECOGNIZER : Int = 3000
    private lateinit var mTextView : TextView
    private var mAnswer : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup random problem
        problemView = findViewById(R.id.sum_text) as TextView
        problemView.text = makeQuestion()

        // touch answer panel to start voice recognition
        mTextView = findViewById(R.id.answer_text)
        mTextView.setOnClickListener{
            startSpeechRecognizer()
        }

        // Click to accept answer
        submitButton = findViewById(R.id.submit_button) as Button
        submitButton.setOnClickListener{
            toast("hi there")
        }
    }
    fun makeQuestion(): String{
        val first : Int = Random.nextInt()
        val second : Int = Random.nextInt()
        mAnswer = first + second
        return first.toString() + " + " + second.toString()
    }

    private fun startSpeechRecognizer() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        //intent.putExtra(RecognizerIntent.EXTRA_PROMPT, mQuestion)
        startActivityForResult(intent, REQUEST_SPEECH_RECOGNIZER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_SPEECH_RECOGNIZER) {
            if (resultCode == Activity.RESULT_OK) {
                val results = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val voiceAnswer = NumberParser().parseInput(results[0]).toInt()

                if (mAnswer == voiceAnswer)
                    mTextView.text = "\n\n" + mAnswer +
                            "\n\ncorrect"
                else
                    mTextView.text = "\n\n" + mAnswer + "\n\nincorrect!"
            }
        }
    }
}