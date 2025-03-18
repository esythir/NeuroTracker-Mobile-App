package com.example.neurotrack.data.service

import com.example.neurotrack.data.local.entity.BehaviorRecord
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import org.json.JSONArray

class GeminiService(private val apiKey: String) {
    private val model by lazy {
        GenerativeModel(
            modelName = "models/gemini-pro",
            apiKey = apiKey
        )
    }

    suspend fun analyzePatterns(records: List<BehaviorRecord>): AnalysisResult = withContext(Dispatchers.IO) {
        try {
            if (records.isEmpty()) {
                return@withContext AnalysisResult(
                    mainInsight = "Ainda não há registros suficientes para análise",
                    patterns = listOf("Adicione mais registros para ver padrões"),
                    triggers = listOf("Continue registrando para identificar gatilhos"),
                    recommendations = listOf(
                        "Mantenha registros regulares",
                        "Observe padrões de comportamento",
                        "Identifique possíveis gatilhos"
                    )
                )
            }

            val recordsData = records.map { record ->
                "ID: ${record.id}, " +
                "Humor: ${record.mood ?: "Não especificado"}, " +
                "Sentimentos: ${record.feelings ?: "Não especificado"}, " +
                "Intensidade: ${record.intensity}, " +
                "Duração: ${record.duration}, " +
                "Gatilho: ${record.trigger ?: "Não especificado"}, " +
                "Observações: ${record.notes ?: "Não especificado"}"
            }.joinToString("\n")

            val prompt = """
                Você é um assistente especializado em análise de comportamentos e padrões emocionais.
                Analise os seguintes registros de comportamento e forneça insights úteis.
                
                Registros:
                $recordsData
                
                Por favor, forneça uma análise estruturada no seguinte formato JSON:
                {
                    "mainInsight": "Um insight principal sobre os padrões observados",
                    "patterns": ["Padrão 1", "Padrão 2", "Padrão 3"],
                    "triggers": ["Gatilho 1", "Gatilho 2", "Gatilho 3"],
                    "recommendations": ["Recomendação 1", "Recomendação 2", "Recomendação 3", "Recomendação 4"]
                }
                
                Todos os textos devem estar em português. Seja específico e baseie suas análises apenas nos dados fornecidos.
            """.trimIndent()

            val response = model.generateContent(
                content {
                    text(prompt)
                }
            )

            val responseText = response.text?.trim() ?: throw Exception("Resposta vazia do Gemini")
            
            val jsonPattern = """\{[\s\S]*\}""".toRegex()
            val jsonMatch = jsonPattern.find(responseText)
            val jsonString = jsonMatch?.value ?: throw Exception("Formato de resposta inválido")
            
            val jsonObject = JSONObject(jsonString)
            
            val patternsArray = jsonObject.optJSONArray("patterns") ?: JSONArray()
            val triggersArray = jsonObject.optJSONArray("triggers") ?: JSONArray()
            val recommendationsArray = jsonObject.optJSONArray("recommendations") ?: JSONArray()
            
            val patterns = mutableListOf<String>()
            val triggers = mutableListOf<String>()
            val recommendations = mutableListOf<String>()
            
            for (i in 0 until patternsArray.length()) {
                patterns.add(patternsArray.getString(i))
            }
            
            for (i in 0 until triggersArray.length()) {
                triggers.add(triggersArray.getString(i))
            }
            
            for (i in 0 until recommendationsArray.length()) {
                recommendations.add(recommendationsArray.getString(i))
            }
            
            return@withContext AnalysisResult(
                mainInsight = jsonObject.optString("mainInsight", "Análise não disponível"),
                patterns = if (patterns.isEmpty()) listOf("Nenhum padrão identificado") else patterns,
                triggers = if (triggers.isEmpty()) listOf("Nenhum gatilho identificado") else triggers,
                recommendations = if (recommendations.isEmpty()) listOf("Nenhuma recomendação disponível") else recommendations
            )

        } catch (e: Exception) {
            return@withContext AnalysisResult(
                mainInsight = "Erro na análise: ${e.message}",
                patterns = listOf("Não foi possível analisar os padrões"),
                triggers = listOf("Não foi possível identificar gatilhos"),
                recommendations = listOf(
                    "Tente novamente mais tarde",
                    "Verifique sua conexão com a internet",
                    "Continue registrando normalmente"
                )
            )
        }
    }
}

data class AnalysisResult(
    val mainInsight: String,
    val patterns: List<String>,
    val triggers: List<String>,
    val recommendations: List<String>
) 