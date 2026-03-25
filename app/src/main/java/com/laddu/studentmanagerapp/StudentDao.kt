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

    //MainThread ->
    //Background Thread

}