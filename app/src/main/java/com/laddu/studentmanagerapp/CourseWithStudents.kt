package com.laddu.studentmanagerapp

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class CourseWithStudents(
    @Embedded val course: Course,
    @Relation(
        parentColumn = "courseId",
        entityColumn = "id",
        associateBy = Junction(
            value = StudentCourseCrossRef::class,
            parentColumn = "courseId",
            entityColumn = "studentId"
        )
    )
    val students: List<Student>
)

