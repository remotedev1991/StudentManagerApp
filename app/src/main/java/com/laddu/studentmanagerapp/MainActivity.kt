package com.laddu.studentmanagerapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private var studentDatabase: StudentDatabase? = null
    private lateinit var adapter: StudentAdapter
    private lateinit var emptyState: View
    private lateinit var studentCountView: TextView

    private val dateFormatter by lazy {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }

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

        val studentRecyclerView = findViewById<RecyclerView>(R.id.recycler)
        val addButton = findViewById<ExtendedFloatingActionButton>(R.id.add)
        emptyState = findViewById(R.id.empty_state)
        studentCountView = findViewById(R.id.student_count)

        studentRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = StudentAdapter(
            onDelete = { student ->
                Thread {
                    studentDatabase?.studentDao()?.deleteStudent(student)
                    loadStudentShowcase()
                }.start()
            },
            onUpdate = { showcase ->
                showStudentDialog(showcase)
            }
        )
        studentRecyclerView.adapter = adapter

        loadStudentShowcase()

        addButton.setOnClickListener {
            showStudentDialog()
        }
    }

    private fun showStudentDialog(studentShowcaseToEdit: StudentShowcase? = null) {
        val dialogView = layoutInflater.inflate(R.layout.add_student_dialog, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.name_input)
        val ageInput = dialogView.findViewById<EditText>(R.id.age_input)
        val gradeInput = dialogView.findViewById<EditText>(R.id.grade_input)
        val admissionDateInput = dialogView.findViewById<EditText>(R.id.admission_date_input)
        val subjectsInput = dialogView.findViewById<EditText>(R.id.subjects_input)
        val coursesInput = dialogView.findViewById<EditText>(R.id.courses_input)
        val studentToEdit = studentShowcaseToEdit?.student

        studentShowcaseToEdit?.let { showcase ->
            val student = showcase.student
            nameInput?.setText(student.name)
            ageInput?.setText(student.age.toString())
            gradeInput?.setText(student.grade)
            admissionDateInput?.setText(dateFormatter.format(student.admissionDate))
            subjectsInput?.setText(showcase.subjects.joinToString(separator = "\n"))
            coursesInput?.setText(showcase.courses.joinToString(separator = "\n"))
        }

        AlertDialog.Builder(this)
            .setTitle(
                if (studentToEdit == null) getString(R.string.add_student_title)
                else getString(R.string.update_student_title)
            )
            .setView(dialogView)
            .setPositiveButton(
                if (studentToEdit == null) getString(R.string.add_button_label)
                else getString(R.string.update_button_label)
            ) { _, _ ->
                val name = nameInput?.text?.toString()?.trim().orEmpty()
                val age = ageInput?.text?.toString()?.trim()?.toIntOrNull() ?: 0
                val grade = gradeInput?.text?.toString()?.trim().orEmpty()
                val date = admissionDateInput?.text?.toString()?.trim().orEmpty()
                val subjectNames = parseItemList(subjectsInput?.text?.toString().orEmpty())
                val courseNames = parseItemList(coursesInput?.text?.toString().orEmpty())
                val admissionDateParsed = dateFormatter.parse(date) ?: java.util.Date()

                val student = Student(
                    id = studentToEdit?.id ?: 0,
                    name = if (name.isBlank()) getString(R.string.default_student_name) else name,
                    age = age,
                    grade = if (grade.isBlank()) getString(R.string.default_grade_label) else grade,
                    admissionDate = admissionDateParsed
                )

                Thread {
                    val database = studentDatabase ?: return@Thread
                    val studentDao = database.studentDao()
                    val subjectDao = database.subjectDao()
                    val enrollmentDao = database.enrollmentDao()

                    database.runInTransaction {
                        val studentId = if (studentToEdit == null) {
                            studentDao.insertStudent(student).toInt()
                        } else {
                            studentDao.updateStudent(student)
                            student.id
                        }

                        subjectDao.deleteSubjectsForStudent(studentId)
                        if (subjectNames.isNotEmpty()) {
                            subjectDao.insertSubjects(
                                subjectNames.map { subjectName ->
                                    Subject(subjectName = subjectName, studentId = studentId)
                                }
                            )
                        }

                        enrollmentDao.deleteEnrollmentsForStudent(studentId)
                        courseNames.forEach { courseName ->
                            val courseId = enrollmentDao.getCourseIdByName(courseName)
                                ?: enrollmentDao.insertCourse(Course(courseName = courseName)).toInt()

                            enrollmentDao.insertEnrollment(
                                StudentCourseCrossRef(studentId = studentId, courseId = courseId)
                            )
                        }
                    }

                    loadStudentShowcase()
                }.start()
            }
            .setNegativeButton(R.string.cancel_button_label, null)
            .show()
    }

    private fun loadStudentShowcase() {
        Thread {
            val studentProfiles = studentDatabase?.studentDao()?.getStudentsWithSubjects().orEmpty()
            val courseProfiles = studentDatabase?.enrollmentDao()?.getStudentsWithCourses().orEmpty()
            val coursesByStudentId = courseProfiles.associateBy { it.student.id }

            val studentCards = studentProfiles.map { profile ->
                StudentShowcase(
                    student = profile.student,
                    subjects = profile.subjects.map(Subject::subjectName),
                    courses = coursesByStudentId[profile.student.id]?.courses.orEmpty().map(Course::courseName)
                )
            }

            runOnUiThread {
                adapter.submitStudents(studentCards)
                val hasStudents = studentCards.isNotEmpty()
                studentCountView.text = getString(R.string.student_count_badge, studentCards.size)
                emptyState.isVisible = !hasStudents
            }
        }.start()
    }

    private fun parseItemList(rawValue: String): List<String> {
        val seenValues = linkedSetOf<String>()
        val parsedValues = mutableListOf<String>()

        rawValue
            .split(Regex("[,\n]"))
            .map(String::trim)
            .filter(String::isNotEmpty)
            .forEach { value ->
                val normalizedValue = value.lowercase(Locale.getDefault())
                if (seenValues.add(normalizedValue)) {
                    parsedValues.add(value)
                }
            }

        return parsedValues
    }
}