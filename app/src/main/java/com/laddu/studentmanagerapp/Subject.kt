package com.laddu.studentmanagerapp

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "subjects",
    foreignKeys = [
        ForeignKey(
            entity = Student::class,
            parentColumns = ["id"],
            childColumns = ["studentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index("studentId")]
)
data class Subject(
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int = 0,
    val subjectName: String,
    val studentId: Int
)


//Foreign key relationship between students and subjects

