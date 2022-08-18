package com.softyorch.taskapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.softyorch.taskapp.navigation.TaskAppNavigation
import com.softyorch.taskapp.ui.theme.TaskAppTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPreferences = getSharedPreferences("remember_me", Context.MODE_PRIVATE)
            TaskApp(sharedPreferences = sharedPreferences)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun TaskApp(sharedPreferences: SharedPreferences) {
    TaskAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TaskAppNavigation(sharedPreferences = sharedPreferences)
        }
    }
}
