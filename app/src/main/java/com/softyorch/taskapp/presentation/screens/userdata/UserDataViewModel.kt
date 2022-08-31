package com.softyorch.taskapp.presentation.screens.userdata

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.UserDataRepository
import com.softyorch.taskapp.utils.StateLogin
import com.softyorch.taskapp.utils.REGEX_PASSWORD
import com.softyorch.taskapp.utils.StandardizedSizes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    private val _userDataActive = MutableLiveData<UserData>()
    //val userDataActive: LiveData<UserData> = _userDataActive

    private val _image = MutableLiveData<String>()
    val image: LiveData<String> = _image

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _pass = MutableLiveData<String>()
    val pass: LiveData<String> = _pass

    private val _saveEnabled = MutableLiveData<Boolean>()
    val saveEnabled: LiveData<Boolean> = _saveEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        _isLoading.value = true
        _userDataActive.value = getUserActiveSharedPreferences()
        Log.d("DATALOAD", "Init._userDataActive -> ${_userDataActive.value}")

        while (_userDataActive.value == null){
            _isLoading.value = true
        }

        loadData()
        _isLoading.value = false
    }

    fun loadData() {
        Log.d("DATALOAD", "Init.loadData -> ${_userDataActive.value}")
        _image.value = _userDataActive.value?.userPicture ?: ""
        _name.value = _userDataActive.value?.username ?: ""
        _email.value = _userDataActive.value?.userEmail ?: ""
        _pass.value = _userDataActive.value?.userPass ?: ""
    }

    suspend fun onDataChange(name: String, email: String, pass: String) {
        _name.value = name
        _email.value = email
        _pass.value = pass
        _saveEnabled.value = isValidName(name = name) &&
                isValidEmail(email = email) &&
                isValidPass(pass = pass)
    }

    fun saveUserImage(image: String) {
        _image.value = image
        _saveEnabled.value = true
    }

    private fun isValidName(name: String): Boolean = name.length >= 3

    private fun isValidPass(pass: String): Boolean =
        Pattern.matches(REGEX_PASSWORD, pass)

    private suspend fun isValidEmail(email: String): Boolean =
        getUserDataEmail(email = email).data?.userEmail.let { !it.isNullOrEmpty() }

    suspend fun onUpdateData(): Boolean {
        _isLoading.value = true

        if (_saveEnabled.value == true) _userDataActive.value?.let { data ->
            data.username = name.value!!
            data.userEmail = email.value!!
            data.userPass = pass.value!!
            data.userPicture = image.value!!.toString()
            Log.d("DATALOAD", "Init.loadData.image -> ${image.value!!}")
            Log.d("DATALOAD", "Init.loadData.imageToUri() -> ${image.value!!.toString()}")
            updateUserData(userData = data)
            delay(500)
            _isLoading.value = false
            return true
        }
        return false
    }


    /**
     * data
     */

    private suspend fun getUserDataEmail(email: String): Resource<UserData> =
        repository.getUserDataEmail(email = email)

    private fun updateUserData(userData: UserData) = viewModelScope.launch {
        repository.updateUserData(userData = userData)
        stateLogin.refreshData(userData = userData)
    }

/*    fun deleteUserData(userData: UserData) = viewModelScope.launch {
        repository.deleteUserData(userData = userData)
    }*/


    /**
     * stateLogin
     */

    fun logOut() = stateLogin.logOut()

    private fun getUserActiveSharedPreferences(): UserData? =
        stateLogin.userDataActive

    fun sizeSelectedOfUser(): StandardizedSizes = stateLogin.getTextSizeSelectedOfUser()
}