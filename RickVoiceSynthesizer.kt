package com.rick.assistant.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import java.util.Locale

class RickVoiceSynthesizer(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var speechRecognizer: SpeechRecognizer? = null

    init {
        tts = TextToSpeech(context, this)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale("pt", "BR")
            
            // Configure Rick's signature voice characteristics:
            // Adult male tone: pitch slightly lowered (0.92f)
            // Energetic, confident, fast speech rate: (1.14f)
            tts?.setPitch(0.92f)
            tts?.setSpeechRate(1.14f)

            // Select natural Brazilian Portuguese male voice if available
            val voices = tts?.voices ?: emptySet()
            val malePtBr = voices.firstOrNull { 
                it.locale == Locale("pt", "BR") && it.name.contains("male", ignoreCase = true) 
            }
            malePtBr?.let { tts?.voice = it }
        }
    }

    fun speak(text: String, onDone: () -> Unit) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "RICK_TTS_ID")
    }

    fun startListening(onResult: (String) -> Unit) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")
        }
        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        speechRecognizer?.stopListening()
    }
}