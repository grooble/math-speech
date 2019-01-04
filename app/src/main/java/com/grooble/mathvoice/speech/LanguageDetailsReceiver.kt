package com.grooble.mathvoice.speech

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent


class LanguageDetailsReceiver(ssl: ShowSupportedLanguages) : BroadcastReceiver() {

    private var mLanguages : List<String>
    private var mSSL : ShowSupportedLanguages

    init {
        mLanguages = ArrayList()
        mSSL = ssl
    }

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent.extras
        mLanguages = extras.getStringArrayList(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES) as java.util.ArrayList
        if (mLanguages == null) {
            mSSL.updateResults("No voice data found.");
        } else {
            var s = "\nList of language voice data:\n"
            for (item in (0..mLanguages.size)) {
                s += (mLanguages[item]+ ", ")
            }
            s += "\n"
            mSSL.updateResults(s)
        }
    }
}