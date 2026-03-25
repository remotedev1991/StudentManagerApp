package com.laddu.studentmanagerapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) // This will automatically generate a unique ID for each student
    val id: Int = 0,
    val name: String,
    val age: Int,
    val grade: String,
    val admissionDate: Date
)
//student 12 -> 12 Harry 20 A
//student 13 -> 13 Ron 21 B
//student 14 -> 14 Hermione 19 A+


//converter