package com.grooble.mathvoice.speech

import android.app.Activity
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.TextView
import android.speech.RecognizerIntent
import com.grooble.mathvoice.R

private lateinit var mTextView : TextView

class ShowSupportedLanguages : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.language_layout)
        if(!SpeechRecognizer.isRecognitionAvailable(this)){
            updateResults("\nNo voice recognition support on your device!")
        }
        else{
            val ldr = LanguageDetailsReceiver(this)
            sendOrderedBroadcast(RecognizerIntent
                .getVoiceDetailsIntent(this), null, ldr, null,
                Activity.RESULT_OK, null, null)
        }
    }
    fun updateResults(s : String){
        mTextView = findViewById(R.id.language_list_text)
        mTextView.text = s
    }
}