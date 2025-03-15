package com.example.neurotrack.data.service

import com.example.neurotrack.data.local.entity.BehaviorRecord
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiService(private val apiKey: String) {
    private val model by lazy {
        GenerativeModel(
            modelName = "models/gemini-pro",  // Caminho completo do modelo
            apiKey = apiKey
        )
    }

    suspend fun analyzePatterns(records: List<BehaviorRecord>): AnalysisResult = withContext(Dispatchers.IO) {
        try {
            if (records.isEmpty()) {
                return@withContext getEmptyAnalysis()
            }

            // Temporariamente retornando análise simulada enquanto resolvemos o Gemini
            return@withContext getSimulatedAnalysis(records)

        } catch (e: Exception) {
            return@withContext getErrorAnalysis(e.message ?: "Erro desconhecido")
        }
    }

    private fun getEmptyAnalysis() = AnalysisResult(
        mainInsight = "Ainda não há registros suficientes para análise",
        patterns = listOf("Adicione mais registros para ver padrões"),
        triggers = listOf("Continue registrando para identificar gatilhos"),
        recommendations = listOf(
            "Mantenha registros regulares",
            "Observe padrões de comportamento",
            "Identifique possíveis gatilhos"
        )
    )

    private fun getErrorAnalysis(error: String) = AnalysisResult(
        mainInsight = "Erro na análise: $error",
        patterns = listOf("Não foi possível analisar os padrões"),
        triggers = listOf("Não foi possível identificar gatilhos"),
        recommendations = listOf(
            "Tente novamente mais tarde",
            "Verifique sua conexão com a internet",
            "Continue registrando normalmente"
        )
    )

    private fun getSimulatedAnalysis(records: List<BehaviorRecord>) = AnalysisResult(
        mainInsight = "Com base na análise dos últimos ${records.size} registros, " +
            "observamos que seus comportamentos têm se mantido consistentes. " +
            "Continue registrando para uma análise mais detalhada.",
        patterns = listOf(
            "Padrão observado nos últimos registros",
            "Comportamentos mais frequentes identificados"
        ),
        triggers = listOf(
            "Possíveis gatilhos identificados",
            "Situações que podem influenciar comportamentos"
        ),
        recommendations = listOf(
            "Continue mantendo registros regulares",
            "Observe padrões de comportamento",
            "Identifique possíveis gatilhos"
        )
    )
}

data class AnalysisResult(
    val mainInsight: String,
    val patterns: List<String>,
    val triggers: List<String>,
    val recommendations: List<String>
) 