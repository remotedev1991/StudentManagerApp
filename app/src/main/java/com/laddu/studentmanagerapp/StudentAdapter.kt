package com.laddu.studentmanagerapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class StudentAdapter(
    private val onDelete: (Student) -> Unit,
    private val onUpdate: (StudentShowcase) -> Unit
) : ListAdapter<StudentShowcase, StudentAdapter.ItemViewHolder>(StudentDiffCallback) {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.name)
        private val ageTextView: TextView = itemView.findViewById(R.id.age)
        private val gradeTextView: TextView = itemView.findViewById(R.id.grade_badge)
        private val admissionDateTextView: TextView = itemView.findViewById(R.id.date)
        private val subjectsTextView: TextView = itemView.findViewById(R.id.subjects_value)
        private val coursesTextView: TextView = itemView.findViewById(R.id.courses_value)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.delete_btn)
        private val editButton: ImageButton = itemView.findViewById(R.id.edit_btn)

        fun bind(
            item: StudentShowcase,
            onDelete: (Student) -> Unit,
            onUpdate: (StudentShowcase) -> Unit
        ) {
            val student = item.student
            val context = itemView.context
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            nameTextView.text = student.name
            ageTextView.text = context.getString(R.string.age_value, student.age)
            gradeTextView.text = context.getString(R.string.grade_badge_value, student.grade)
            admissionDateTextView.text = context.getString(
                R.string.admission_date_value,
                format.format(student.admissionDate)
            )
            subjectsTextView.text = item.subjects.ifEmpty {
                listOf(context.getString(R.string.no_subjects_assigned))
            }.joinToString(separator = "  •  ")
            coursesTextView.text = item.courses.ifEmpty {
                listOf(context.getString(R.string.no_courses_assigned))
            }.joinToString(separator = "  •  ")

            deleteButton.setOnClickListener { onDelete(student) }
            editButton.setOnClickListener { onUpdate(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), onDelete, onUpdate)
    }

    fun submitStudents(students: List<StudentShowcase>) {
        submitList(students)
    }

    companion object {
        private val StudentDiffCallback = object : DiffUtil.ItemCallback<StudentShowcase>() {
            override fun areItemsTheSame(oldItem: StudentShowcase, newItem: StudentShowcase): Boolean {
                return oldItem.student.id == newItem.student.id
            }

            override fun areContentsTheSame(oldItem: StudentShowcase, newItem: StudentShowcase): Boolean {
                return oldItem == newItem
            }
        }
    }
}
