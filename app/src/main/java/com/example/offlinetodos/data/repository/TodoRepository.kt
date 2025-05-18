package com.example.offlinetodos.data.repository

import com.example.offlinetodos.TodoApiService
import com.example.offlinetodos.data.local.TodoDao
import com.example.offlinetodos.data.local.toDomain
import com.example.offlinetodos.data.local.toEntity
import com.example.offlinetodos.domain.model.Todo

class TodoRepository(private val api: TodoApiService, private val dao: TodoDao) {

    suspend fun getTodos(): List<Todo> {
        return try {
            val remoteTodos = api.getTodos()
            dao.insertTodos(remoteTodos.map { it.toEntity() })
            remoteTodos
        } catch (e: Exception) {
            dao.getAllTodos().map { it.toDomain() }
        }
    }

    suspend fun getTodoById(id: Int): Todo = dao.getTodoById(id).toDomain()

    suspend fun addTodo(todo: Todo) {
        dao.addTodo(todo.toEntity())
    }

    suspend fun updateTodo(todo: Todo) {
        dao.updateTodo(todo.toEntity())
    }

    suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo.toEntity())
    }
}
