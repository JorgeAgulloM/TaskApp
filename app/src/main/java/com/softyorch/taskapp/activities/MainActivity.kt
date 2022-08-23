package com.softyorch.taskapp.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.softyorch.taskapp.navigation.TaskAppNavigation
import com.softyorch.taskapp.ui.theme.TaskAppTheme
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val viewModel = hiltViewModel<MainActivityViewModel>()
            viewModel.loadSharePreferences(sharedPreferences = sharedPreferences)
            val settingList = viewModel.loadSettings()
            TaskApp(settingList = settingList)
        }
    }
}

/**
 * [0] String("last_login_date", Date.from(Instant.now()).toString())
 * [1] Boolean("remember_me", false)
 * [2] Boolean("light_dark_automatic_theme", true)
 * [3] Boolean("light_or_dark_theme", false)
 * [4] Boolean("automatic_language", true)
 * [5] Boolean("automatic_colors", false)
 * [6] Long("time_limit_auto_loading", 604800000L)
 * [7] Int("text_size", 0)
 * */
@ExperimentalMaterial3Api
@Composable
fun TaskApp(settingList: List<Any>) {

    val darkSystem by rememberSaveable { mutableStateOf(settingList[2] as Boolean) }
    val light by rememberSaveable { mutableStateOf(settingList[3] as Boolean) }
    val colorSystem by rememberSaveable { mutableStateOf(settingList[5] as Boolean) }

    TaskAppTheme(
        darkTheme = if (darkSystem) isSystemInDarkTheme()
        else light,
        dynamicColor = colorSystem
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TaskAppNavigation()
        }
    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
fun reloadApp(context: MainActivity){
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
    context.finish()
    Runtime.getRuntime().exit(0)
}*/
