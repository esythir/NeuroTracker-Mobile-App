package com.example.neurotrack.data.repository

import android.content.Context
import com.example.neurotrack.data.local.dao.BehaviorRecordDao
import com.example.neurotrack.data.local.entity.BehaviorRecord
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DataExportRepository(
    private val behaviorRecordDao: BehaviorRecordDao,
    private val context: Context
) {
    suspend fun exportDataToCSV(): Result<File> = withContext(Dispatchers.IO) {
        try {
            // 1. Obter todos os registros do banco de dados
            val records = behaviorRecordDao.getAllBehaviorRecords().first()
            
            // 2. Criar arquivo CSV diretamente
            val csvFile = createCSVFile(records)
            
            // 3. Retornar o arquivo CSV
            Result.success(csvFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun createCSVFile(records: List<BehaviorRecord>): File {
        // Criar um arquivo CSV
        val csvFile = File(context.cacheDir, "behavior_records.csv")
        
        // Criar o conteúdo CSV
        val csvContent = buildString {
            // Cabeçalho
            append("ID,Behavior ID,Timestamp,Mood,Feelings,Intensity,Duration,Trigger,Notes\n")
            
            // Dados
            records.forEach { record ->
                val timestamp = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(record.timestamp),
                    ZoneId.systemDefault()
                ).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                
                append("${record.id},")
                append("${record.behaviorId},")
                append("\"$timestamp\",")
                append("\"${record.mood?.replace("\"", "\"\"") ?: ""}\",")
                append("\"${record.feelings?.replace("\"", "\"\"") ?: ""}\",")
                append("${record.intensity},")
                append("\"${record.duration}\",")
                append("\"${record.trigger?.replace("\"", "\"\"") ?: ""}\",")
                append("\"${record.notes?.replace("\"", "\"\"") ?: ""}\"\n")
            }
        }
        
        // Escrever o conteúdo CSV no arquivo
        FileOutputStream(csvFile).use { it.write(csvContent.toByteArray()) }
        
        return csvFile
    }
    
    suspend fun clearAllData(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // Apagar todos os registros do banco de dados
            behaviorRecordDao.deleteAllBehaviorRecords()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 