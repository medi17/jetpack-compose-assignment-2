
package com.example.offlinetodos.presentation.screens
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.offlinetodos.presentation.viewmodel.DetailViewModel
import com.example.offlinetodos.presentation.viewmodel.TodoViewModel

@Composable
fun TodoDetailScreen(todoId: Int, viewModel: DetailViewModel, navController: NavController) {
    val todo = viewModel.todo

    Column(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        if (todo != null) {
            Text("Title: ${todo.title}")
            Text("Status: ${if (todo.completed) "Completed" else "Incomplete"}")
            Text("User ID: ${todo.userId}")
        }
        else {
            CircularProgressIndicator()
        }
    }
}