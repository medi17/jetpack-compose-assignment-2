package com.example.offlinetodos.presentation.viewmodel

import com.example.offlinetodos.data.repository.TodoRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope

import com.example.offlinetodos.domain.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class DetailViewModel(private val repository: TodoRepository, private val todoId: Int) : ViewModel() {
    var todo by mutableStateOf<Todo?>(null)

    init {
        viewModelScope.launch {
            todo = repository.getTodoById(todoId)
        }
    }
}
