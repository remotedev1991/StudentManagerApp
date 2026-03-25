package com.laddu.studentmanagerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    var studentDatabase: StudentDatabase? = null

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

        studentDatabase = StudentDatabase.getInstance(this)

        val studentDao = studentDatabase?.studentDao()
        val subjectDao = studentDatabase?.subjectDao()

        val recyckerview = findViewById<RecyclerView>(R.id.recycler)
        recyckerview.layoutManager = LinearLayoutManager(this)

        val adapter = StudentAdapter(
            onDelete = { student ->
                Thread {
                    studentDao?.deleteStudent(student) //deletion
                    val students =
                        studentDao?.getAllStudents() //fetching the updated list of students after deletion

                    runOnUiThread {
                        val adapter = recyckerview.adapter as StudentAdapter
                        adapter.submitStudents(students)
                    }
                }.start()
            },
            onUpdate = { student ->

                val dialogView = layoutInflater.inflate(R.layout.add_student_dialog, null)

                val nameInput = dialogView.findViewById<EditText>(R.id.name_input)
                val ageInput = dialogView.findViewById<EditText>(R.id.age_input)
                val gradeInput = dialogView.findViewById<EditText>(R.id.grade_input)
                val admissionDateInput =
                    dialogView.findViewById<EditText>(R.id.admission_date_input)

                nameInput?.setText(student.name)
                ageInput?.setText(student.age.toString())
                gradeInput?.setText(student.grade)
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dateString = sdf.format(student.admissionDate)
                admissionDateInput?.setText(dateString)

                val dialog = AlertDialog.Builder(this)
                    .setTitle("Update Student")
                    .setView(dialogView)
                    .setPositiveButton("Update") { dialog, _ ->
                        val nameInput =
                            (dialog as AlertDialog).findViewById<EditText>(R.id.name_input)
                        val ageInput = dialog.findViewById<EditText>(R.id.age_input)
                        val gradeInput = dialog.findViewById<EditText>(R.id.grade_input)
                        val dateInput = dialog.findViewById<EditText>(R.id.admission_date_input)

                        val name = nameInput?.text.toString()
                        val age = ageInput?.text.toString().toIntOrNull() ?: 0
                        val grade = gradeInput?.text.toString()
                        val date = dateInput?.text.toString()

                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val admissionDateParsed = dateFormat.parse(date) ?: java.util.Date()

                        val student = student.copy(
                            name = name,
                            age = age,
                            grade = grade,
                            admissionDate = admissionDateParsed
                        )

                        Thread {
                            studentDao?.updateStudent(student)
                            val students = studentDao?.getAllStudents()
                            runOnUiThread {
                                val adapter = recyckerview.adapter as StudentAdapter
                                adapter.submitStudents(students)
                            }
                        }.start()
                    }
                    .setNegativeButton("Cancel", null)

                dialog.show()

            }
        )
        recyckerview.adapter = adapter

        //First time fetching of students from the database and displaying in the recyclerview
        Thread {
            val students = studentDao?.getAllStudents()
            Log.d("TAG", "${Thread.currentThread().name}")
            runOnUiThread {
                Log.d("TAG", "${Thread.currentThread().name}")
                adapter.submitStudents(students)
            }
        }.start()

        val fab =
            findViewById<FloatingActionButton>(R.id.add)


        fab.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Add Student")
                .setView(R.layout.add_student_dialog)
                .setPositiveButton("Add") { dialog, _ ->
                    val nameInput =
                        (dialog as AlertDialog).findViewById<EditText>(R.id.name_input)
                    val ageInput = dialog.findViewById<EditText>(R.id.age_input)
                    val gradeInput = dialog.findViewById<EditText>(R.id.grade_input)
                    val admissionDateInput =
                        dialog.findViewById<EditText>(R.id.admission_date_input)

                    val name = nameInput?.text.toString()
                    val age = ageInput?.text.toString().toIntOrNull() ?: 0
                    val grade = gradeInput?.text.toString()
                    val admissionDate = admissionDateInput?.text.toString()

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val admissionDateParsed = dateFormat.parse(admissionDate) ?: java.util.Date()
                    val student = Student(
                        name = name, age = age, grade = grade,
                        admissionDate = admissionDateParsed
                    )
                    Thread {
                        val studentID = studentDao?.insertStudent(student)
                        subjectDao?.insertSubject(
                            Subject(
                                subjectName = "Maths",
                                studentId = (studentID ?: 0L).toInt()
                            )
                        )
                        val students = studentDao?.getAllStudents()
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