package com.rick.assistant.data

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.*

class GeminiBrainClient(private val httpClient: HttpClient, private val apiKey: String) {

    private val RICK_SYSTEM_PROMPT = """
        Você é o 'Rick — Assistente Inteligente Beta'.
        PERSONALIDADE:
        - Curioso, inteligente, respostas rápidas e naturais em pt-BR.
        - Humor sarcástico e irreverente sem ser ofensivo.
        - Sabe quando ser sério (segurança, finanças, emergências).
        - NUNCA finja ter realizado uma ação que não realizou.
        - Identifique fontes de pesquisa na web e distinga de conhecimento próprio.
        - Salve apenas memórias explicitamente autorizadas com [SALVAR_MEMORIA: texto].
    """.trimIndent()

    suspend fun queryRickBrain(
        userPrompt: String,
        memories: List<String>,
        isCarMode: Boolean
    ): RickBrainResponse {
        val payload = buildJsonObject {
            put("model", "gemini-3.8-flash")
            putJsonObject("systemInstruction") {
                put("text", RICK_SYSTEM_PROMPT + "\n[MEMÓRIAS DO USUÁRIO]: " + memories.joinToString("; "))
            }
            putJsonArray("tools") {
                addJsonObject { putJsonObject("googleSearch") {} }
            }
        }

        val response = httpClient.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent?key=$apiKey") {
            contentType(ContentType.Application.Json)
            setBody(payload.toString())
        }

        return parseGeminiResponse(response.bodyAsText())
    }
}

data class RickBrainResponse(
    val cleanText: String,
    val searchSources: List<String>,
    val memoryToSave: String?,
    val commands: List<String>
)