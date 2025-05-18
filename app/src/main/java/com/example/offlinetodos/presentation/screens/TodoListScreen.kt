package com.example.offlinetodos.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.offlinetodos.presentation.viewmodel.TodoViewModel
import androidx.compose.runtime.getValue
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavController
import com.example.offlinetodos.domain.model.Todo

@Composable
fun TodoListScreen(navController: NavController, viewModel: TodoViewModel) {
    val todos by viewModel.todos.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var editTodo by remember { mutableStateOf<Todo?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = {
            editTodo = null
            showDialog = true
        }) {
            Text("Add TODO")
        }

        Spacer(Modifier.height(16.dp))

        when {
            isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            error != null -> Text("Error: $error", color = Color.Red)
            else -> LazyColumn {
                items(todos) { todo ->
                    TodoCard(
                        todo = todo,
                        onEdit = {
                            editTodo = it
                            showDialog = true
                        },
                        onDelete = { todo -> viewModel.deleteTodo(todo) },
                        onClick = { navController.navigate("detail/${todo.id}") }
                    )
                }
            }
        }

        if (showDialog) {
            TodoDialog(
                initialTodo = editTodo,
                onDismiss = { showDialog = false },
                onSave = {
                    if (editTodo == null) viewModel.addTodo(it)
                    else viewModel.updateTodo(it)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun TodoCard(todo: Todo, onEdit: (Todo) -> Unit, onDelete: (Todo) -> Unit, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (todo.completed) Color(0xFFE0F2F1) else Color(0xFFFFF9C4)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(todo.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(if (todo.completed) "Completed" else "Incomplete")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { onEdit(todo) }, modifier = Modifier.weight(1f)) {
                    Text("Edit")
                }
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { onDelete(todo) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Pink)
                ) {
                    Text("Delete", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun TodoDialog(initialTodo: Todo?, onDismiss: () -> Unit, onSave: (Todo) -> Unit) {
    var title by remember { mutableStateOf(TextFieldValue(initialTodo?.title ?: "")) }
    var completed by remember { mutableStateOf(initialTodo?.completed ?: false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = {
                val newTodo = Todo(
                    userId = 1,
                    id = initialTodo?.id ?: (0..100000).random(),
                    title = title.text,
                    completed = completed
                )
                onSave(newTodo)
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        title = { Text(if (initialTodo == null) "Add TODO" else "Edit TODO") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = completed, onCheckedChange = { completed = it })
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Completed")
                }
            }
        }
    )
}


