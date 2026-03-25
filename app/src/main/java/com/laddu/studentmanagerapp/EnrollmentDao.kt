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

    @Query(
        "SELECT courses.* FROM courses " +
            "INNER JOIN student_course_cross_ref ON courses.courseId = student_course_cross_ref.courseId " +
            "WHERE student_course_cross_ref.studentId = :studentId ORDER BY courses.courseName ASC"
    )
    fun getCoursesForStudent(studentId: Int): List<Course>

    @Query("DELETE FROM student_course_cross_ref WHERE studentId = :studentId")
    fun deleteEnrollmentsForStudent(studentId: Int)

    @Transaction
    @Query("SELECT * FROM students")
    fun getStudentsWithCourses(): List<StudentWithCourses>

    @Transaction
    @Query("SELECT * FROM courses")
    fun getCoursesWithStudents(): List<CourseWithStudents>
}

