package com.example.samtasks.ui.fragments.create_edit

import androidx.lifecycle.ViewModel
import com.example.samtasks.data.db.TasksDao
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val tasksDao: TasksDao
): ViewModel() {

}