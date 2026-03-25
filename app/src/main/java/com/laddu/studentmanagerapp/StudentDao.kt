package com.laddu.studentmanagerapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface StudentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStudent(student: Student): Long

    @Query("SELECT * FROM students")
    fun getAllStudents(): List<Student>

    @Update
    fun updateStudent(student: Student)

    @Delete
    fun deleteStudent(student: Student)

    @Transaction
    @Query("SELECT * FROM students")
    fun getStudentsWithSubjects(): List<StudentWithSubjects>

    @Transaction
    @Query("SELECT * FROM students WHERE LOWER(name) LIKE LOWER(:searchName) ORDER BY name ASC")
    fun searchStudentsByName(searchName: String): List<StudentWithSubjects>

    @Transaction
    @Query("SELECT * FROM students WHERE grade = :grade ORDER BY name ASC")
    fun searchStudentsByGrade(grade: String): List<StudentWithSubjects>

    @Transaction
    @Query("SELECT * FROM students WHERE age >= :minAge AND age <= :maxAge ORDER BY name ASC")
    fun searchStudentsByAgeRange(minAge: Int, maxAge: Int): List<StudentWithSubjects>

    //MainThread ->
    //Background Thread

}

