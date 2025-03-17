package com.example.neurotrack.ui.screens.dashboard

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.min

// Componentes personalizados para substituir os não encontrados
@Composable
fun LineChart(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier
) {
    // Implementação simplificada de um gráfico de linha
    Box(modifier = modifier.fillMaxWidth().height(200.dp)) {
        Text("Gráfico de linha - dados simulados", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun BarChart(
    data: List<Pair<String, Int>>,
    modifier: Modifier = Modifier
) {
    // Implementação simplificada de um gráfico de barras
    Box(modifier = modifier.fillMaxWidth().height(200.dp)) {
        Text("Gráfico de barras - dados simulados", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun PieChart(
    data: Map<String, Int>,
    colors: Map<String, Color>
) {
    val totalValue = data.values.sum().toFloat()
    
    Canvas(
        modifier = Modifier
            .size(240.dp)
            .padding(8.dp)
    ) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val radius = min(canvasWidth, canvasHeight) / 2
        val center = Offset(canvasWidth / 2, canvasHeight / 2)
        
        var startAngle = 0f
        
        data.forEach { (category, value) ->
            val sweepAngle = 360f * (value / totalValue)
            val color = colors[category] ?: Color.Gray
            
            drawArc(
                color = color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2)
            )
            
            startAngle += sweepAngle
        }
    }
}

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Análise de Comportamento",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Card de resumo
            SummaryCard(totalRecords = state.totalRecords)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Card de insights
            InsightsCard(patterns = state.identifiedPatterns)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Card de intensidade ao longo do tempo
            IntensityOverTimeCard(data = state.intensityData)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Card de distribuição de humor
            MoodDistributionCard(
                data = state.moodDistribution,
                colors = mapOf(
                    "Ótimo" to Color(0xFF4CAF50),
                    "Bem" to Color(0xFF8BC34A),
                    "Neutro" to Color(0xFFFFEB3B),
                    "Mal" to Color(0xFFFF9800),
                    "Péssimo" to Color(0xFFF44336)
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Novo card: Recomendações Personalizadas
            PersonalizedRecommendationsCard(recommendations = state.personalizedRecommendations)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Novo card: Padrões de Gatilhos
            TriggerPatternsCard(triggerAnalysis = state.triggerAnalysis)
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Novo card: Progresso e Tendências
            ProgressTrendsCard(weeklyTrend = state.weeklyTrend, nextStepsSuggestions = state.nextStepsSuggestions)
        }
    }
}

@Composable
fun SummaryCard(totalRecords: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F0F7)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Resumo dos últimos 30 dias",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$totalRecords registros realizados",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun InsightsCard(patterns: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F0F7)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Insights Principais",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            patterns.forEach { pattern ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "• ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = pattern,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun IntensityOverTimeCard(data: List<Pair<String, Float>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F0F7)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Intensidade ao Longo do Tempo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Implementação do gráfico de linha
            LineChartImpl(
                data = data,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}

@Composable
fun LineChartImpl(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return
    
    Box(
        modifier = modifier
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val maxValue = data.maxOf { it.second }
            val minValue = data.minOf { it.second }
            val range = maxValue - minValue
            
            // Desenhar linhas de grade horizontais
            val gridColor = Color.LightGray.copy(alpha = 0.5f)
            for (i in 0..5) {
                val y = height - (height * i / 5f)
                drawLine(
                    color = gridColor,
                    start = Offset(0f, y),
                    end = Offset(width, y),
                    strokeWidth = 1f
                )
            }
            
            // Desenhar o caminho da linha
            val path = Path()
            val points = data.mapIndexed { index, (_, value) ->
                val x = width * index / (data.size - 1)
                val normalizedValue = (value - minValue) / range
                val y = height - (normalizedValue * height)
                Offset(x, y)
            }
            
            // Mover para o primeiro ponto
            if (points.isNotEmpty()) {
                path.moveTo(points.first().x, points.first().y)
                
                // Conectar os pontos restantes
                for (i in 1 until points.size) {
                    path.lineTo(points[i].x, points[i].y)
                }
                
                // Desenhar a linha
                drawPath(
                    path = path,
                    color = Color(0xFF673AB7),
                    style = Stroke(
                        width = 4f,
                        cap = StrokeCap.Round
                    )
                )
                
                // Desenhar pontos
                points.forEach { point ->
                    drawCircle(
                        color = Color(0xFF673AB7),
                        radius = 6f,
                        center = point
                    )
                }
            }
        }
        
        // Valores do eixo Y
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.End
        ) {
            val maxValue = data.maxOf { it.second }
            val minValue = data.minOf { it.second }
            val range = maxValue - minValue
            
            for (i in 5 downTo 0) {
                val value = minValue + (range * i / 5f)
                Text(
                    text = String.format("%.1f", value),
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                if (i > 0) Spacer(modifier = Modifier.weight(1f))
            }
        }
        
        // Datas no eixo X
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val step = maxOf(1, data.size / 5)
            for (i in 0 until data.size step step) {
                Text(
                    text = data[i].first,
                    fontSize = 10.sp,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.width(40.dp)
                )
            }
        }
        
        // Legenda
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(Color(0xFF673AB7), shape = RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Intensidade",
                fontSize = 12.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun MoodDistributionCard(
    data: Map<String, Int>,
    colors: Map<String, Color>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF3F0F7)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Distribuição de Humor",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            PieChart(
                data = data,
                colors = colors
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Legenda
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                colors.forEach { (mood, color) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(color, shape = RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = mood,
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PersonalizedRecommendationsCard(
    recommendations: List<Recommendation> = emptyList()
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Recomendações Personalizadas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            recommendations.forEach { recommendation ->
                Text(
                    text = recommendation.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = recommendation.description,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TriggerPatternsCard(
    triggerAnalysis: Map<String, Float> = emptyMap()
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Análise de Gatilhos",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Com base nos seus registros, identificamos os seguintes gatilhos potenciais:",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            triggerAnalysis.forEach { (trigger, percentage) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val color = when {
                        percentage > 70f -> Color(0xFFF44336) // Vermelho
                        percentage > 50f -> Color(0xFFFF9800) // Laranja
                        else -> Color(0xFFFFEB3B) // Amarelo
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Column {
                        Text(
                            text = trigger,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${percentage.toInt()}% das crises",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun ProgressTrendsCard(
    weeklyTrend: Double = 0.0,
    nextStepsSuggestions: List<String> = emptyList()
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Progresso e Tendências",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val trendText = when {
                weeklyTrend > 0.5 -> "Estabilidade nos padrões de comportamento. Continue a introduzir novas técnicas de regulação."
                weeklyTrend < -0.5 -> "Melhoria nos padrões de comportamento. Continue com as estratégias atuais."
                else -> "Padrões de comportamento estáveis. Continue monitorando."
            }
            
            Text(
                text = trendText,
                style = MaterialTheme.typography.bodyMedium,
                color = when {
                    weeklyTrend > 0.5 -> Color(0xFFFF9800) // Laranja
                    weeklyTrend < -0.5 -> Color(0xFF4CAF50) // Verde
                    else -> Color(0xFF000080) // Azul escuro (substituindo o amarelo)
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Próximos Passos Sugeridos:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            nextStepsSuggestions.forEach { suggestion ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "• ")
                    Text(
                        text = suggestion,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}