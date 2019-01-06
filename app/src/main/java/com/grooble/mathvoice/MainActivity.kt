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
import android.view.View
import android.widget.RadioButton
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug


class MainActivity : AppCompatActivity(), AnkoLogger {

    private val operations = arrayOf("addition", "subtraction", "multiplication", "division")
    private lateinit var problemView : TextView
    private lateinit var answerView: TextView
    private lateinit var resultView: TextView
    private val REQUEST_SPEECH_RECOGNIZER : Int = 3000
    private var mAnswer : Int = 0
    private var operation = "addition"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup initial problem: addition
        problemView = findViewById(R.id.sum_text)
        problemView.text = makeQuestion(operations[0])

        // touch answer panel to start voice recognition
        answerView = findViewById(R.id.answer_text)
        answerView.setOnClickListener{
            startSpeechRecognizer()
        }

        // touch question panel to load new question
        problemView.setOnClickListener {
            problemView.text = makeQuestion(operation)
            answerView.text = "answer"
        }

        // initialize result text view
        resultView = findViewById(R.id.result_text)

    }

    // respond to clicks of the radio button selector
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.addition_button ->
                    if (checked) {
                        operation = operations[0]
                    }
                R.id.subtraction_button ->
                    if (checked) {
                        operation = operations[1]
                    }
                R.id.multiplication_button ->
                    if (checked) {
                        operation = operations[2]
                    }
            }
        }
    }

    // return String representing the question
    fun makeQuestion(operation : String): String{
        var first : Int = Random.nextInt(1, 10)
        var second : Int = Random.nextInt(1, 10)

        return when (operations.indexOf(operation)) {
            1 -> {
                if(first < second){
                    var temp = first
                    first = second
                    second = temp
                }
                mAnswer = first - second
                first.toString() + " - " + second.toString()
            }
            2 -> {
                mAnswer = first*second
                first.toString() + " X " + second.toString()
            }
            else -> {
                mAnswer = first + second
                first.toString() + " + " + second.toString()
            }
        }
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
                val voiceAnswer = results[0]
                debug("""voice answer: $voiceAnswer""")
                val answerList = voiceAnswer.toString().split(" ")
                var answerNumber = ""

                if(answerList.size==1){
                    answerNumber = answerList[0]
                }
                else {
                    for (i in answerList.indices) {
                        if ((answerList[i] == "equals") && ((answerList.size - 1) > i)) {
                            answerNumber = answerList[answerList.lastIndex]
                        }
                    }
                }

                val answerNumberCheck = answerNumber.toIntOrNull()
                if(answerNumberCheck == null){
                    answerView.text = "try again?"
                }
                if (mAnswer == answerNumberCheck) {
                    answerView.text = answerNumberCheck.toString()
                    resultView.text = getString(R.string.correct)
                }
                else {
                    answerView.text = answerNumberCheck.toString()
                    resultView.text = getString(R.string.incorrect)
                }
            }
        }
    }
}