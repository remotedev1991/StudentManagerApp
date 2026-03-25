package com.laddu.studentmanagerapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter: RecyclerView.Adapter<StudentAdapter.ItemViewHolder>() {

    var students: List<Student> = emptyList()

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val nameTextView: android.widget.TextView = itemView.findViewById(R.id.name)
        val ageTextView: android.widget.TextView = itemView.findViewById(R.id.age)
        val gradeTextView: android.widget.TextView = itemView.findViewById(R.id.grade)
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
    }

    fun submitStudents(students: List<Student>) {
        this.students = students
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return students.size
    }

}
