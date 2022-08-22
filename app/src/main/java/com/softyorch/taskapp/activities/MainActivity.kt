package com.softyorch.taskapp.activities

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
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
 * String("last_login_date", Date.from(Instant.now()).toString())
 * Boolean("remember_me", false)
 * Boolean("light_dark_automatic_theme", true)
 * Boolean("light_or_dark_theme", false)
 * Boolean("automatic_language", true)
 * Boolean("automatic_colors", false)
 * Long("time_limit_auto_loading", 604800000L)
 * Int("text_size", 0)
 * */
@ExperimentalMaterial3Api
@Composable
fun TaskApp(settingList: List<Any>) {
    TaskAppTheme(
        darkTheme = if (settingList[2] as Boolean) isSystemInDarkTheme()
        else settingList[3] as Boolean,
        dynamicColor = settingList[4] as Boolean
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TaskAppNavigation()
        }
    }
}
