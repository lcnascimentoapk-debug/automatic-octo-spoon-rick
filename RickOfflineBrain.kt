package com.rick.assistant.offline

import android.content.Context
import com.rick.assistant.data.MemoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Motor Neural Nativo Offline do Rick (Android On-Device / Edge Computing)
 * Processa intenções de hardware, memórias autorizadas, biometria e diálogo
 * mesmo sem conexão com a internet ou em modo avião.
 */
class RickOfflineBrain(
    private val context: Context,
    private val memoryRepo: MemoryRepository
) {
    suspend fun processOffline(
        prompt: String,
        batteryPct: Int,
        isTorchOn: Boolean
    ): OfflineResult = withContext(Dispatchers.Default) {
        val lower = prompt.trim().lowercase()

        when {
            lower.contains("lanterna") && (lower.contains("liga") || lower.contains("acende")) -> {
                OfflineResult(
                    text = "Lanterna ativada em modo offline pelo motor nativo!",
                    command = "lanterna_on"
                )
            }
            lower.contains("lanterna") && (lower.contains("desliga") || lower.contains("apaga")) -> {
                OfflineResult(
                    text = "Lanterna desligada. Economizando bateria offline.",
                    command = "lanterna_off"
                )
            }
            lower.contains("pix") || lower.contains("transfer") || lower.contains("banco") -> {
                OfflineResult(
                    text = "Operação sensível detectada! Exigindo autenticação biométrica local no Android Biometrics Prompt.",
                    needsBiometrics = "Operação Financeira Local"
                )
            }
            lower.contains("lembre-se") || lower.contains("guarde") -> {
                val fact = prompt.replace(Regex("(?i)^(rick,?|por favor,?|lembre-se que|guarde que|\\s+)+"), "").trim()
                memoryRepo.saveMemory(fact, "pessoal")
                OfflineResult(
                    text = "Fato gravado no Room Database local offline com sucesso: \"$fact\"",
                    savedMemory = fact
                )
            }
            lower.contains("bateria") -> {
                OfflineResult(
                    text = "Telemetria local offline: Bateria em $batteryPct%."
                )
            }
            lower.contains("modo carro") || lower.contains("direção") -> {
                OfflineResult(
                    text = "Iniciando HUD Veicular Offline!",
                    command = "modo_carro"
                )
            }
            else -> {
                OfflineResult(
                    text = "Comando processado localmente no Android sem conexão com a internet. Resposta instantânea e privada!"
                )
            }
        }
    }
}

data class OfflineResult(
    val text: String,
    val command: String? = null,
    val savedMemory: String? = null,
    val needsBiometrics: String? = null
)