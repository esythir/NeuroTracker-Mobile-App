package com.example.neurotrack.data.local.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.example.neurotrack.data.local.entity.Feeling

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromFeelingsList(feelings: List<Feeling>?): String? {
        return feelings?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toFeelingsList(feelingsString: String?): List<Feeling>? {
        return feelingsString?.let {
            val type = object : TypeToken<List<Feeling>>() {}.type
            gson.fromJson(it, type)
        }
    }
} 