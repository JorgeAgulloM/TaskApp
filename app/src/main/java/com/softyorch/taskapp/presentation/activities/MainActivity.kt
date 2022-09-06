package com.softyorch.taskapp.presentation.activities

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager.*
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.softyorch.taskapp.R.string.*
import com.softyorch.taskapp.presentation.navigation.TaskAppNavigation
import com.softyorch.taskapp.presentation.theme.TaskAppTheme
import com.softyorch.taskapp.utils.*
import com.softyorch.taskapp.utils.sdk29AndUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.IOException

private val _newImageGallery = MutableLiveData<String>()
val newImageGallery: LiveData<String> = _newImageGallery

@ExperimentalMaterial3Api
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var readPermissionGranted = false
    private var writePermissionGranted = false
    private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                readPermissionGranted = permissions[READ_EXTERNAL_STORAGE] ?: readPermissionGranted
                writePermissionGranted =
                    permissions[WRITE_EXTERNAL_STORAGE] ?: writePermissionGranted
            }
        updateOrRequestPermissions()

        setContent {
            val sharedPreferences = getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            val viewModel = hiltViewModel<MainActivityViewModel>()
            viewModel.loadSharePreferences(sharedPreferences = sharedPreferences)
            val settingList = viewModel.loadSettings()
            val reloadComposable: () -> Unit = { this.recreate() }
            val coroutineScope = rememberCoroutineScope()
            var imageResult: String? = newImageGallery.observeAsState().value
            val getImage: () -> Unit = {
                coroutineScope.launch {
                    coroutineScope.launch {
                        getImageGallery.launch(GALLERY_IMAGES)
                    }.let {
                        it.join()
                        imageResult = newImageGallery.value
                    }
                }
            }

            val getUserImage: Pair<() -> Unit, String?> = Pair(getImage, imageResult)

            TaskApp(
                settingList = settingList,
                reloadComposable = reloadComposable,
                getUserImage = getUserImage
            )
        }
    }

    private val getImageGallery = registerForActivityResult(GetContent()) { uri ->
        if (uri != null) _newImageGallery.value = uri.toString()
    }

    private fun updateOrRequestPermissions() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            this,
            READ_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            this,
            WRITE_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

        readPermissionGranted = hasReadPermission
        writePermissionGranted = hasWritePermission || minSdk29

        val permissionsToRequest = mutableListOf<String>()

        if (!writePermissionGranted) permissionsToRequest.add(WRITE_EXTERNAL_STORAGE)
        if (!readPermissionGranted) permissionsToRequest.add(READ_EXTERNAL_STORAGE)

        if (permissionsToRequest.isNotEmpty())
            permissionsLauncher.launch(permissionsToRequest.toTypedArray())
    }

    private fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap): Boolean {
        val imageCollection = sdk29AndUp {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.WIDTH, bmp.width)
            put(MediaStore.Images.Media.HEIGHT, bmp.height)
        }

        return try {
            contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                contentResolver.openOutputStream(uri).use { outputStream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)) {
                        throw IOException(getString(io_exception_save_bitmap))
                    }
                }
            } ?: throw IOException(getString(io_exception_create_media_store_entry))
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
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
private fun TaskApp(
    settingList: List<Any>,
    reloadComposable: () -> Unit,
    getUserImage: Pair<() -> Unit, String?>
) {

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
            TaskAppNavigation(reloadComposable = reloadComposable, getUserImage = getUserImage)
        }
    }
}

