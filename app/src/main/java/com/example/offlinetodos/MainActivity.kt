package com.example.offlinetodos

import com.example.offlinetodos.data.repository.TodoRepository
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.NavType

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.offlinetodos.data.local.TodoDatabase
import com.example.offlinetodos.presentation.screens.TodoDetailScreen
import com.example.offlinetodos.presentation.screens.TodoListScreen
import com.example.offlinetodos.presentation.viewmodel.TodoViewModel
import com.example.offlinetodos.presentation.viewmodel.DetailViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = TodoDatabase.getDatabase(this)
        val repository = TodoRepository(RetrofitInstance.api, db.todoDao())

        setContent {
            val navController = rememberNavController()

            NavHost(navController, startDestination = "list") {
                composable("list") {
                    val viewModel = remember { TodoViewModel(repository) }
                    TodoListScreen(navController, viewModel)
                }
                composable("detail/{todoId}", arguments = listOf(navArgument("todoId") {
                    type = NavType.IntType })) { backStackEntry ->
                    val todoId = backStackEntry.arguments?.getInt("todoId") ?: 0
                    val viewModel = remember { DetailViewModel(repository, todoId) }
                    TodoDetailScreen(todoId, viewModel, navController)
                }
            }
        }
    }
}
