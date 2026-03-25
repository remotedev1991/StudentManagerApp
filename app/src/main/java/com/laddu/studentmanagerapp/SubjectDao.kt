package com.laddu.studentmanagerapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubject(subject: Subject)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubjects(subjects: List<Subject>)

    @Query("SELECT * FROM subjects WHERE studentId = :studentId ORDER BY subjectName ASC")
    fun getSubjectsForStudent(studentId: Int): List<Subject>

    @Query("DELETE FROM subjects WHERE studentId = :studentId")
    fun deleteSubjectsForStudent(studentId: Int)


}