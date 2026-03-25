package com.laddu.studentmanagerapp

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class StudentWithCourses(
    @Embedded val student: Student,
    @Relation(
        parentColumn = "id",
        entityColumn = "courseId",
        associateBy = Junction(
            value = StudentCourseCrossRef::class,
            parentColumn = "studentId",
            entityColumn = "courseId"
        )
    )
    val courses: List<Course>
)

