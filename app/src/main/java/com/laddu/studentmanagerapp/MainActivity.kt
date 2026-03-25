package com.laddu.studentmanagerapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.core.widget.addTextChangedListener
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private companion object {
        const val MIN_AGE = 3
        const val MAX_AGE = 100
        const val DEFAULT_AGE = 18
    }

    private var studentDatabase: StudentDatabase? = null
    private lateinit var adapter: StudentAdapter
    private lateinit var emptyState: View
    private lateinit var studentCountView: TextView
    private var allStudents: List<StudentShowcase> = emptyList()

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

        val searchInput = findViewById<EditText>(R.id.search_input)
        searchInput?.addTextChangedListener { editable ->
            val query = editable.toString().trim()
            filterStudents(query)
        }

        loadStudentShowcase()

        addButton.setOnClickListener {
            showStudentDialog()
        }
    }

    private fun showStudentDialog(studentShowcaseToEdit: StudentShowcase? = null) {
        val dialogView = layoutInflater.inflate(R.layout.add_student_dialog, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.name_input)
        val agePicker = dialogView.findViewById<NumberPicker>(R.id.age_picker)
        val gradeChipGroup = dialogView.findViewById<ChipGroup>(R.id.grade_chip_group)
        val admissionDateInput = dialogView.findViewById<EditText>(R.id.admission_date_input)
        val admissionDateInputLayout = dialogView.findViewById<TextInputLayout>(R.id.admission_date_input_layout)
        val subjectsInput = dialogView.findViewById<EditText>(R.id.subjects_input)
        val coursesInput = dialogView.findViewById<EditText>(R.id.courses_input)
        val studentToEdit = studentShowcaseToEdit?.student
        var selectedAdmissionDate = studentToEdit?.admissionDate ?: Date()

        agePicker.minValue = MIN_AGE
        agePicker.maxValue = MAX_AGE
        agePicker.wrapSelectorWheel = false
        agePicker.value = (studentToEdit?.age ?: DEFAULT_AGE).coerceIn(MIN_AGE, MAX_AGE)
        gradeChipGroup.check(findGradeChipId(studentToEdit?.grade ?: getString(R.string.default_grade_label), gradeChipGroup))

        admissionDateInput?.setText(dateFormatter.format(selectedAdmissionDate))

        val openDatePicker = {
            showAdmissionDatePicker(selectedAdmissionDate) { updatedDate ->
                selectedAdmissionDate = updatedDate
                admissionDateInput?.setText(dateFormatter.format(updatedDate))
            }
        }

        admissionDateInput?.setOnClickListener { openDatePicker() }
        admissionDateInputLayout?.setEndIconOnClickListener { openDatePicker() }
        admissionDateInputLayout?.setOnClickListener { openDatePicker() }

        studentShowcaseToEdit?.let { showcase ->
            val student = showcase.student
            nameInput?.setText(student.name)
            gradeChipGroup.check(findGradeChipId(student.grade, gradeChipGroup))
            admissionDateInput?.setText(dateFormatter.format(selectedAdmissionDate))
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
                val age = agePicker.value
                val selectedGradeChip = dialogView.findViewById<Chip>(gradeChipGroup.checkedChipId)
                val grade = selectedGradeChip?.text?.toString()?.trim().orEmpty()
                val subjectNames = parseItemList(subjectsInput?.text?.toString().orEmpty())
                val courseNames = parseItemList(coursesInput?.text?.toString().orEmpty())

                val student = Student(
                    id = studentToEdit?.id ?: 0,
                    name = if (name.isBlank()) getString(R.string.default_student_name) else name,
                    age = age,
                    grade = if (grade.isBlank()) getString(R.string.default_grade_label) else grade,
                    admissionDate = selectedAdmissionDate
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

            allStudents = studentProfiles.map { profile ->
                StudentShowcase(
                    student = profile.student,
                    subjects = profile.subjects.map(Subject::subjectName),
                    courses = coursesByStudentId[profile.student.id]?.courses.orEmpty().map(Course::courseName)
                )
            }

            runOnUiThread {
                adapter.submitStudents(allStudents)
                val hasStudents = allStudents.isNotEmpty()
                studentCountView.text = getString(R.string.student_count_badge, allStudents.size)
                emptyState.isVisible = !hasStudents
            }
        }.start()
    }

    private fun filterStudents(query: String) {
        if (query.isBlank()) {
            adapter.submitStudents(allStudents)
            studentCountView.text = getString(R.string.student_count_badge, allStudents.size)
            emptyState.isVisible = allStudents.isEmpty()
            return
        }

        val filteredStudents = allStudents.filter { showcase ->
            val student = showcase.student
            val nameMatches = student.name.contains(query, ignoreCase = true)
            val gradeMatches = student.grade.contains(query, ignoreCase = true)
            val ageMatches = student.age.toString().contains(query)
            val subjectMatches = showcase.subjects.any { it.contains(query, ignoreCase = true) }
            val courseMatches = showcase.courses.any { it.contains(query, ignoreCase = true) }

            nameMatches || gradeMatches || ageMatches || subjectMatches || courseMatches
        }

        adapter.submitStudents(filteredStudents)
        studentCountView.text = getString(R.string.student_count_badge, filteredStudents.size)
        emptyState.isVisible = filteredStudents.isEmpty()
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

    private fun showAdmissionDatePicker(initialDate: Date, onDateSelected: (Date) -> Unit) {
        val calendar = Calendar.getInstance().apply {
            time = initialDate
        }

        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                onDateSelected(selectedCalendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun findGradeChipId(grade: String, chipGroup: ChipGroup): Int {
        for (index in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(index) as? Chip ?: continue
            if (chip.text.toString().equals(grade, ignoreCase = true)) {
                return chip.id
            }
        }

        return R.id.grade_chip_a
    }
}