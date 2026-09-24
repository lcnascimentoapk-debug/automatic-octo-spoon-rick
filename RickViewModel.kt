package com.rick.assistant.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rick.assistant.data.GeminiBrainClient
import com.rick.assistant.data.MemoryRepository
import com.rick.assistant.hardware.AndroidToolsBridge
import com.rick.assistant.voice.RickVoiceSynthesizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class VoiceState { IDLE, LISTENING, THINKING, SPEAKING }

data class Message(
    val id: String,
    val sender: String,
    val text: String,
    val searchSources: List<String> = emptyList()
)

data class RickUiState(
    val messages: List<Message> = emptyList(),
    val voiceState: VoiceState = VoiceState.IDLE,
    val isTorchOn: Boolean = false,
    val batteryPct: Int = 100
)

class RickViewModel(
    private val geminiClient: GeminiBrainClient,
    private val memoryRepo: MemoryRepository,
    private val voiceSynthesizer: RickVoiceSynthesizer,
    private val androidBridge: AndroidToolsBridge
) : ViewModel() {

    private val _uiState = MutableStateFlow(RickUiState())
    val uiState: StateFlow<RickUiState> = _uiState.asStateFlow()

    fun sendMessage(prompt: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(voiceState = VoiceState.THINKING)
            
            // Fetch explicit authorized memories to inject into prompt
            val userMemories = memoryRepo.getAllAuthorizedMemories()
            
            // Query Gemini Brain with Google Search grounding enabled
            val result = geminiClient.queryRickBrain(
                userPrompt = prompt,
                memories = userMemories,
                isCarMode = false
            )

            // Speak response with original Brazilian male raspy voice
            voiceSynthesizer.speak(result.cleanText) {
                _uiState.value = _uiState.value.copy(voiceState = VoiceState.IDLE)
            }
        }
    }

    fun toggleTorch() {
        val newState = !_uiState.value.isTorchOn
        androidBridge.setFlashlight(newState)
        _uiState.value = _uiState.value.copy(isTorchOn = newState)
    }

    fun onVoiceOrbToggled() {
        if (_uiState.value.voiceState == VoiceState.LISTENING) {
            voiceSynthesizer.stopListening()
            _uiState.value = _uiState.value.copy(voiceState = VoiceState.IDLE)
        } else {
            _uiState.value = _uiState.value.copy(voiceState = VoiceState.LISTENING)
            voiceSynthesizer.startListening { transcribed ->
                sendMessage(transcribed)
            }
        }
    }
}