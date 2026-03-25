package com.laddu.studentmanagerapp

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface SubjectDao {

    @Insert
    fun insertSubject(subject: Subject)


}