package com.example.kou_proje

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TaskViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    init {
        _tasks.value = listOf(
            Task(1, "Görev 1", "Görev açıklaması 1", false),
            Task(2, "Görev 2", "Görev açıklaması 2", true)
        )
    }

    fun addTask(task: Task) {
        _tasks.value = _tasks.value + task
    }

    fun updateTask(task: Task) {
        _tasks.value = _tasks.value.map {
            if (it.id == task.id) task else it
        }
    }
}