package com.grooble.mathvoice

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import kotlin.random.*
import android.speech.RecognizerIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Build
import android.provider.MediaStore
import android.speech.RecognitionListener
import android.view.View
import android.widget.RadioButton
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import android.speech.SpeechRecognizer
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Gravity
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import java.util.*
import kotlin.random.Random


class MainActivity : Activity(), AnkoLogger, RecognitionListener {

    private val PERMISSIONS_REQUEST_INTERNET = 300
    private val PERMISSIONS_REQUEST_RECORD_AUDIO = 400
    private val ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 500

    private val operations = arrayOf("addition", "subtraction", "multiplication", "random")
    private lateinit var problemView : TextView
    private lateinit var answerView: TextView
    private lateinit var resultView: TextView
    private var mAnswer : Int = 0
    private var operation = "addition"
    private lateinit var speech: SpeechRecognizer

    override fun onRmsChanged(p0: Float) {}
    override fun onBufferReceived(p0: ByteArray?) {}
    override fun onPartialResults(p0: Bundle?) {}
    override fun onEvent(p0: Int, p1: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onEndOfSpeech() {}

    override fun onError(errorCode: Int) {

        when (errorCode) {

            SpeechRecognizer.ERROR_AUDIO ->
                info(R.string.error_audio)

            SpeechRecognizer.ERROR_CLIENT ->
                info(R.string.error_client)

            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                info(R.string.error_insufficient_permissions)

            SpeechRecognizer.ERROR_NETWORK ->
                info(R.string.error_network)

            SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                info(R.string.error_network_timeout)

            SpeechRecognizer.ERROR_NO_MATCH ->
                info(R.string.error_no_match)

            SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                info(R.string.error_recognizer_busy)

            SpeechRecognizer.ERROR_SERVER ->
                info(R.string.error_server)

            SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                info(R.string.error_speech_timeout)

            else ->
                info(R.string.error_understand)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(speech != null){
            speech.stopListening()
            speech.cancel()
            speech.destroy()
        }
    }
    override fun onResults(data: Bundle?) {
        info("in onResults")
        val results = data!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val voiceAnswer = results[0]
        info("""voice answer: $voiceAnswer""")
        // for English
        //val answerList = voiceAnswer.toString().split(" ")
        var answerNumber = ""

        /*
        if (answerList.size == 1) {
            info("answer size = ${answerList.size}")
            answerNumber = answerList[0]
        } else {
            for (i in answerList.indices) {
                println("components: $i")
                if (((answerList[i] == "は")||(answerList[i] == "=")) && ((answerList.size - 1) > i)) {
                    answerNumber = answerList[answerList.lastIndex]
                }
            } */
            // for Japanese
        // answer is not valid Int, so must be a sentence
        if(voiceAnswer.toIntOrNull() == null) {
            info("voiceAnswer not parsable to Int")
            val index = voiceAnswer.lastIndexOf("は")
            if (index != -1) {
                info("found \"は\" in answer")
                answerNumber = voiceAnswer.substring(index + 1)
                info("answer: $answerNumber")
            } else
            // test for "=" as sum delimiter, instead of "は"
            {
                info("testing for \"=\" in voiceAnswer")
                val index2 = voiceAnswer.lastIndexOf("=")
                if (index2 != -1) {
                    info("found \"=\" in voiceAnswer")
                    answerNumber = voiceAnswer.substring(index2 + 1)
                    info("answer = $answerNumber")
                }
            }
        }
        // Int returned so can convert
        else {
            info("voiceAnswer parsable to Int: ${voiceAnswer.trim()}")
            answerNumber = voiceAnswer.trim()
        }

        val answerNumberCheck = answerNumber.toIntOrNull()
        if (answerNumberCheck == null) {
            answerView.text = "try again?"
        }
        if (mAnswer == answerNumberCheck) {
            answerView.text = answerNumberCheck.toString()
            resultView.text = getString(R.string.correct)
            resultView.gravity = Gravity.CENTER
            resultView.typeface = Typeface.DEFAULT
            resultView.backgroundColor = ContextCompat.getColor(this, R.color.correctBlue)
            resultView.textColor = ContextCompat.getColor(this, R.color.white)

        } else {
            answerView.text = answerNumberCheck.toString()
            resultView.text = getString(R.string.incorrect)
            resultView.gravity = Gravity.CENTER
            resultView.typeface = Typeface.DEFAULT
            resultView.backgroundColor = ContextCompat.getColor(this, R.color.incorrectRed)
            resultView.textColor = ContextCompat.getColor(this, R.color.white)
        }
    }

    override fun onReadyForSpeech(p0: Bundle?) {
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setup speech recognition
        speech = SpeechRecognizer.createSpeechRecognizer(this)
        speech.setRecognitionListener(this)


        // setup initial problem: addition
        problemView = findViewById(R.id.sum_text)
        problemView.text = makeQuestion(operations[0])

        // touch answer panel to start voice recognition
        answerView = findViewById(R.id.answer_text)
        answerView.setOnClickListener{
            info("calling startListening in onCreate")
            startSpeechRecognizer()
        }

        // touch question panel to load new question
        problemView.setOnClickListener {
            problemView.text = makeQuestion(operation)
            answerView.text = "answer"
            resultView.text = ""
            resultView.backgroundColor = ContextCompat.getColor(this, R.color.white)
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
                R.id.random_button ->
                    if (checked) {
                        operation = operations[3]
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
            3 -> {
                val rand : Int = Random.nextInt(0, 3)
                info("Random: $rand")
                makeQuestion(operations[rand])
            }
            else -> {
                mAnswer = first + second
                first.toString() + " + " + second.toString()
            }
        }
    }

    @TargetApi(23)
    fun startSpeechRecognizer() {
        info("useInternet")
        val permissionRequest = ArrayList<String>()
        var permissionCode = 0

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (ContextCompat.checkSelfPermission(
                this, Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            // the above needs to check for already received permissions
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.INTERNET
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionRequest.add(Manifest.permission.INTERNET)
                permissionCode = PERMISSIONS_REQUEST_INTERNET
            }

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionRequest.add(Manifest.permission.RECORD_AUDIO)
                permissionCode = PERMISSIONS_REQUEST_RECORD_AUDIO
            }

            if (permissionRequest.size != 0) {
                if (permissionRequest.size > 1) {
                    permissionCode = ASK_MULTIPLE_PERMISSION_REQUEST_CODE
                }
                info("Requesting permissions")
                requestPermissions(permissionRequest.toTypedArray(), permissionCode)
            }
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            // Start intent for camera app on click of user profile pic
            info( "Permissions OK; calling speech recognition intent")
            var intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ja_JP");
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.packageName)

            speech.startListening(intent)
        }
    }
}