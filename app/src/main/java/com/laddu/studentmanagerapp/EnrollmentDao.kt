package com.laddu.studentmanagerapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface EnrollmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCourse(course: Course): Long

    @Query("SELECT courseId FROM courses WHERE courseName = :courseName LIMIT 1")
    fun getCourseIdByName(courseName: String): Int?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertEnrollment(crossRef: StudentCourseCrossRef)

    @Transaction
    @Query("SELECT * FROM students")
    fun getStudentsWithCourses(): List<StudentWithCourses>

    @Transaction
    @Query("SELECT * FROM courses")
    fun getCoursesWithStudents(): List<CourseWithStudents>
}

