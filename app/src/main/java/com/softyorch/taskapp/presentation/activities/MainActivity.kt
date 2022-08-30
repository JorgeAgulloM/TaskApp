package com.softyorch.taskapp.presentation.activities

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.softyorch.taskapp.presentation.navigation.TaskAppNavigation
import com.softyorch.taskapp.presentation.theme.TaskAppTheme
import com.softyorch.taskapp.utils.ImageController
import dagger.hilt.android.AndroidEntryPoint


private val _newImageUser = MutableLiveData<Uri?>(null)
val newImageUser: LiveData<Uri?> = _newImageUser

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val CODE_ACTIVITY = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sharedPreferences = getSharedPreferences("user_data", Context.MODE_PRIVATE)
            val viewModel = hiltViewModel<MainActivityViewModel>()
            viewModel.loadSharePreferences(sharedPreferences = sharedPreferences)
            val settingList = viewModel.loadSettings()
            val reloadComposable: () -> Unit = { this.recreate() }
            //val getImage: () -> Unit = { getImageFromGallery() }

            val getImage: () -> Unit = {
                cosa.launch("image/*")
            }

            TaskApp(
                settingList = settingList,
                reloadComposable = reloadComposable,
                getImage = getImage
            )
        }
    }

    val cosa = registerForActivityResult(GetContent()) { uri ->
        _newImageUser.value = uri
    }

    fun getImageFromGallery() = ImageController.selectPhotoFromGallery(this, CODE_ACTIVITY)

/*    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                //imageUri = data!!.data
                _newImageUser.postValue(data!!.data)
            }
        }
    }*/
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
fun TaskApp(settingList: List<Any>, reloadComposable: () -> Unit, getImage: () -> Unit) {

    val darkSystem by remember { mutableStateOf(settingList[2] as Boolean) }
    val light by remember { mutableStateOf(settingList[3] as Boolean) }
    val colorSystem by remember { mutableStateOf(settingList[5] as Boolean) }

    TaskAppTheme(
        darkTheme = if (darkSystem) isSystemInDarkTheme()
        else light,
        dynamicColor = colorSystem
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TaskAppNavigation(reloadComposable = reloadComposable, getImage = getImage)
        }
    }
}

