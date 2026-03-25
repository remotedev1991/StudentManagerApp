package com.laddu.studentmanagerapp

import androidx.room.Embedded
import androidx.room.Relation

data class StudentWithSubjects(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "id",
        entityColumn = "studentId"
    )
    val subjects: List<Subject>
)

//1:M
