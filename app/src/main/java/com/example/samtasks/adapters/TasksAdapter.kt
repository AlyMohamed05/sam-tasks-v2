package com.example.samtasks.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.samtasks.data.models.Task
import com.example.samtasks.databinding.TodoItemBinding

class TasksAdapter : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffUtilCallback()) {

    class TaskViewHolder private constructor(private val binding: TodoItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.task = task
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TaskViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = TodoItemBinding.inflate(
                    inflater,
                    parent,
                    false
                )
                return TaskViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class TaskDiffUtilCallback : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}