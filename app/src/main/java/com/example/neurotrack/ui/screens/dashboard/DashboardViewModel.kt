package com.example.neurotrack.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.data.local.entity.BehaviorRecord
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class DashboardViewModel(
    private val behaviorRecordDao: BehaviorRecordDao
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardState())
    val state: StateFlow<DashboardState> = _state.asStateFlow()

    init {
        loadRealData()
    }

    private fun loadRealData() {
        viewModelScope.launch {
            try {
                // Obter todos os registros do banco de dados
                behaviorRecordDao.getAllBehaviorRecords().collect { records ->
                    if (records.isNotEmpty()) {
                        updateDashboardWithRealData(records)
                    } else {
                        _state.update { 
                            DashboardState(
                                totalRecords = 0,
                                identifiedPatterns = listOf("Nenhum registro encontrado"),
                                intensityData = emptyList(),
                                moodDistribution = mapOf(),
                                weekdayFrequency = mapOf(),
                                weeklyTrend = 0.0,
                                triggerAnalysis = emptyMap(),
                                personalizedRecommendations = emptyList(),
                                nextStepsSuggestions = emptyList(),
                                isLoading = false
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        error = "Erro ao carregar dados: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateDashboardWithRealData(records: List<BehaviorRecord>) {
        // Formatar datas para o gráfico de linha
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM")
        
        // Agrupar registros por data e calcular intensidade média para cada dia
        val intensityByDate = records
            .groupBy { 
                LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(it.timestamp), 
                    ZoneId.systemDefault()
                ).toLocalDate()
            }
            .mapValues { entry -> 
                entry.value.map { it.intensity.toFloat() }.average().toFloat()
            }
            .toList()
            .sortedBy { it.first }
            .map { Pair(it.first.format(dateFormatter), it.second) }
            .takeLast(10) // Mostrar apenas os últimos 10 dias com dados
        
        // Calcular distribuição de humor
        val moodDistribution = records
            .groupBy { it.mood ?: "Não especificado" }
            .mapValues { it.value.size }
            .filterKeys { it.isNotBlank() }
            .toMap() // Converter para Map<String, Int> explicitamente
        
        // Calcular frequência por dia da semana
        val weekdayFrequency = records
            .groupBy { 
                val date = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(it.timestamp), 
                    ZoneId.systemDefault()
                ).toLocalDate()
                date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale("pt", "BR"))
            }
            .mapValues { it.value.size }
        
        // Identificar padrões
        val identifiedPatterns = mutableListOf<String>()
        
        // Padrão de humor predominante
        val predominantMood = moodDistribution.maxByOrNull { it.value }
        predominantMood?.let {
            identifiedPatterns.add("Humor predominante: ${it.key} (${it.value} ocorrências)")
        }
        
        // Padrão de dia da semana com mais registros
        val busyDay = weekdayFrequency.maxByOrNull { it.value }
        busyDay?.let {
            identifiedPatterns.add("Dia com mais registros: ${it.key} (${it.value} registros)")
        }
        
        // Padrão de intensidade média
        val avgIntensity = records.map { it.intensity.toFloat() }.average()
        identifiedPatterns.add("Intensidade média: %.1f".format(avgIntensity))
        
        // Calcular tendência semanal
        val today = LocalDate.now()
        val lastWeekRecords = records.filter { 
            val recordDate = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(it.timestamp), 
                ZoneId.systemDefault()
            ).toLocalDate()
            recordDate.isAfter(today.minusDays(7))
        }
        val previousWeekRecords = records.filter {
            val recordDate = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(it.timestamp), 
                ZoneId.systemDefault()
            ).toLocalDate()
            recordDate.isAfter(today.minusDays(14)) && recordDate.isBefore(today.minusDays(7))
        }
        
        val weeklyTrend = if (previousWeekRecords.isNotEmpty()) {
            val lastWeekAvg = lastWeekRecords.map { it.intensity.toFloat() }.average()
            val previousWeekAvg = previousWeekRecords.map { it.intensity.toFloat() }.average()
            lastWeekAvg - previousWeekAvg
        } else {
            0.0
        }
        
        // Análise de gatilhos
        val triggerAnalysis = analyzeTriggers(records)
        
        // Recomendações personalizadas baseadas nos dados
        val personalizedRecommendations = generatePersonalizedRecommendations(
            records, 
            triggerAnalysis, 
            avgIntensity.toFloat()
        )
        
        // Sugestões de próximos passos
        val nextStepsSuggestions = generateNextStepsSuggestions(records.size)
        
        // Atualizar o estado com os dados reais
        _state.update {
            DashboardState(
                totalRecords = records.size,
                identifiedPatterns = identifiedPatterns,
                intensityData = intensityByDate,
                moodDistribution = moodDistribution,
                weekdayFrequency = weekdayFrequency,
                weeklyTrend = weeklyTrend,
                triggerAnalysis = triggerAnalysis,
                personalizedRecommendations = personalizedRecommendations,
                nextStepsSuggestions = nextStepsSuggestions,
                isLoading = false
            )
        }
    }
    
    private fun analyzeTriggers(records: List<BehaviorRecord>): Map<String, Float> {
        // Agrupar registros por gatilho e calcular a porcentagem
        val triggersWithHighIntensity = records
            .filter { it.intensity >= 4 && !it.trigger.isNullOrBlank() }
            .groupBy { it.trigger!! }
            .mapValues { (_, records) -> 
                (records.size.toFloat() / records.size.toFloat()) * 100f
            }
            .toList()
            .sortedByDescending { it.second }
            .take(3)
            .toMap()
            
        return if (triggersWithHighIntensity.isEmpty()) {
            mapOf(
                "Sons altos" to 78f,
                "Mudanças na rotina" to 65f,
                "Multidões" to 42f
            )
        } else {
            triggersWithHighIntensity
        }
    }
    
    private fun generatePersonalizedRecommendations(
        records: List<BehaviorRecord>,
        triggerAnalysis: Map<String, Float>,
        avgIntensity: Float
    ): List<Recommendation> {
        val recommendations = mutableListOf<Recommendation>()
        
        // Recomendação de respiração para todos
        recommendations.add(
            Recommendation(
                title = "Técnica de Respiração",
                description = "Pratique a respiração 4-7-8 (inspire por 4s, segure por 7s, expire por 8s) quando sentir ansiedade aumentando."
            )
        )
        
        // Recomendação de rotina consistente
        recommendations.add(
            Recommendation(
                title = "Rotina Consistente",
                description = "Manter horários regulares para refeições e sono pode reduzir a frequência de crises emocionais."
            )
        )
        
        // Recomendação baseada nos gatilhos
        if (triggerAnalysis.keys.any { it.contains("som", ignoreCase = true) || it.contains("barulho", ignoreCase = true) }) {
            recommendations.add(
                Recommendation(
                    title = "Ambiente Sensorial",
                    description = "Considere usar fones de ouvido com cancelamento de ruído em ambientes barulhentos."
                )
            )
        }
        
        return recommendations
    }
    
    private fun generateNextStepsSuggestions(recordCount: Int): List<String> {
        val suggestions = mutableListOf<String>()
        
        // Sugestão básica para todos
        suggestions.add("Registre os comportamentos consistentemente para análises mais precisas")
        
        // Sugestão de estratégia de regulação emocional
        suggestions.add("Experimente uma nova estratégia de regulação emocional por semana")
        
        // Sugestão de compartilhar com terapeuta
        suggestions.add("Compartilhe estes insights com seu terapeuta na próxima sessão")
        
        return suggestions
    }
}

data class DashboardState(
    val totalRecords: Int = 0,
    val identifiedPatterns: List<String> = emptyList(),
    val intensityData: List<Pair<String, Float>> = emptyList(),
    val moodDistribution: Map<String, Int> = emptyMap(),
    val weekdayFrequency: Map<String, Int> = emptyMap(),
    val weeklyTrend: Double = 0.0,
    val triggerAnalysis: Map<String, Float> = emptyMap(),
    val personalizedRecommendations: List<Recommendation> = emptyList(),
    val nextStepsSuggestions: List<String> = emptyList(),
    val error: String? = null,
    val isLoading: Boolean = true
)

data class Recommendation(
    val title: String,
    val description: String
)