package com.example.offlinetodos.presentation.viewmodel

import com.example.offlinetodos.data.repository.TodoRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.offlinetodos.domain.model.Todo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {

    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        fetchTodos()
    }

    fun fetchTodos() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val data = repository.getTodos()
                _todos.value = data
            } catch (e: Exception) {
                _error.value = "Failed to load todos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.addTodo(todo)
                // Refresh the list after adding
                val updatedTodos = repository.getTodos()
                _todos.value = updatedTodos
            } catch (e: Exception) {
                _error.value = "Failed to add todo: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.updateTodo(todo)
                // Refresh the list after updating
                val updatedTodos = repository.getTodos()
                _todos.value = updatedTodos
            } catch (e: Exception) {
                _error.value = "Failed to update todo: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                repository.deleteTodo(todo)
                // Refresh the list after deleting
                val updatedTodos = repository.getTodos()
                _todos.value = updatedTodos
            } catch (e: Exception) {
                _error.value = "Failed to delete todo: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

