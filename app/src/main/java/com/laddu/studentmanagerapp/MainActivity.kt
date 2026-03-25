package com.laddu.studentmanagerapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

class MainActivity : AppCompatActivity() {

    lateinit var studentDatabase: StudentDatabase

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        studentDatabase = Room.databaseBuilder(
            this,
            StudentDatabase::class.java,
            "student_database"
        ).build()

        val studentDao = studentDatabase.studentDao()

        val recyckerview = findViewById<RecyclerView>(R.id.recycler)
        recyckerview.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        val adapter = StudentAdapter()
        recyckerview.adapter = adapter
        val fab = findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.add)


        fab.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Add Student")
                .setView(R.layout.add_student_dialog)
                .setPositiveButton("Add") { dialog, _ ->
                    val nameInput = (dialog as AlertDialog).findViewById<android.widget.EditText>(R.id.name_input)
                    val ageInput = dialog.findViewById<android.widget.EditText>(R.id.age_input)
                    val gradeInput = dialog.findViewById<android.widget.EditText>(R.id.grade_input)

                    val name = nameInput?.text.toString()
                    val age = ageInput?.text.toString().toIntOrNull() ?: 0
                    val grade = gradeInput?.text.toString()

                    val student = Student(name = name, age = age, grade = grade)
                    Thread {
                        studentDao.insertStudent(student)
                        val students  = studentDao.getAllStudents()
                        runOnUiThread {
                            adapter.submitStudents(students)
                        }
                    }.start()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}