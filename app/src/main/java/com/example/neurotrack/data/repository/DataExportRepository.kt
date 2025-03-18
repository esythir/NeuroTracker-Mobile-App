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
            val records = behaviorRecordDao.getAllBehaviorRecords().first()
            
            val csvFile = createCSVFile(records)
            
            Result.success(csvFile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun createCSVFile(records: List<BehaviorRecord>): File {
        val csvFile = File(context.cacheDir, "behavior_records.csv")
        
        val csvContent = buildString {
            append("ID,Behavior ID,Timestamp,Mood,Feelings,Intensity,Duration,Trigger,Notes\n")
            
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
        
        FileOutputStream(csvFile).use { it.write(csvContent.toByteArray()) }
        
        return csvFile
    }
    
    suspend fun clearAllData(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            behaviorRecordDao.deleteAllBehaviorRecords()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 