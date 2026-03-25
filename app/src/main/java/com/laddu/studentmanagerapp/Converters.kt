package com.laddu.studentmanagerapp

import androidx.room.TypeConverter
import java.util.Date

class Converters {

    @TypeConverter
    fun fromDateToTimeStamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun timeStampToDate(timeStamp: Long) : Date {
        return Date(timeStamp)
    }
}