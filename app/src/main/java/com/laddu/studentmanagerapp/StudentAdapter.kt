package com.laddu.studentmanagerapp

import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class StudentAdapter(val onDelete: (Student) -> Unit, val onUpdate: (Student) -> Unit): RecyclerView.Adapter<StudentAdapter.ItemViewHolder>() {

    var students: List<Student> = emptyList()

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: android.widget.TextView = itemView.findViewById(R.id.name)
        val ageTextView: android.widget.TextView = itemView.findViewById(R.id.age)
        val gradeTextView: android.widget.TextView = itemView.findViewById(R.id.grade)

        val admissionDate: android.widget.TextView = itemView.findViewById(R.id.date)

        val delete: ImageButton = itemView.findViewById(R.id.delete_btn)

        val edit: ImageButton = itemView.findViewById(R.id.edit_btn)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): ItemViewHolder {
        val view = android.view.LayoutInflater.from(parent.context).inflate(R.layout.student_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val student = students[position]
        holder.nameTextView.text = student.name
        holder.ageTextView.text = "Age: ${student.age}"
        holder.gradeTextView.text = "Grade: ${student.grade}"

        val format = SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        val text = format.format(student.admissionDate)

        holder.admissionDate.text = "Admission Date: $text"
        holder.delete.setOnClickListener {
            onDelete(student)
        }

        holder.edit.setOnClickListener {
            onUpdate(student)
        }

    }

    fun submitStudents(students: List<Student>?) {
        this.students = students!!
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return students.size
    }

    //added
    //fetched
    //deletion
    //updation
}
