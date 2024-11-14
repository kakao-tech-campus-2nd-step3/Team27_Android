package com.jnu.togetherpet.di

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kakao.vectormap.LatLng
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TypeConverterModule {

    private val gson = Gson()
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    @TypeConverter
    fun fromLatLngList(latLngList: List<LatLng>?): String {
        return gson.toJson(latLngList)
    }

    @TypeConverter
    fun toLatLngList(latLngListString: String): List<LatLng>? {
        val type = object : TypeToken<List<LatLng>>() {}.type
        return gson.fromJson(latLngListString, type)
    }

    @TypeConverter
    fun fromStringToList(value: String): List<String> {
        return gson.fromJson(value, object : TypeToken<List<String>>() {}.type)
    }

    @TypeConverter
    fun fromListToString(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate): String {
        return localDate.format(dateFormatter)
    }

    @TypeConverter
    fun toLocalDate(dateString: String): LocalDate {
        return LocalDate.parse(dateString, dateFormatter)
    }

    @TypeConverter
    fun fromStringToLocalDateTime(value: String): LocalDateTime {
        return value.let { LocalDateTime.parse(it, formatter) }
    }

    @TypeConverter
    fun fromLocalDateTimeToString(date: LocalDateTime): String {
        return date.format(formatter)
    }
}