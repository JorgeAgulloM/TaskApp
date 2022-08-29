package com.softyorch.taskapp.presentation.screens.userdata

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.UserDataRepository
import com.softyorch.taskapp.utils.StateLogin
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin
) : ViewModel() {
    private val _userDataActive = MutableLiveData<UserData>()
    val userDataActive: LiveData<UserData> = _userDataActive

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _pass = MutableLiveData<String>()
    val pass: LiveData<String> = _pass

    private val _image = MutableLiveData<String>()
    val image: LiveData<String> = _image

    private val _saveEnabled = MutableLiveData<Boolean>()
    val saveEnabled: LiveData<Boolean> = _saveEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loadScreen = MutableLiveData<Boolean>()
    val loadScreen: LiveData<Boolean> = _loadScreen

    init {

        viewModelScope.launch(IO) {
            _userDataActive.postValue(getUserActive())
            while (userDataActive.value == null){
                _loadScreen.postValue(true)
            }

            _name.postValue(_userDataActive.value?.username ?: "")
            _email.postValue(_userDataActive.value?.userEmail ?: "")
            _pass.postValue(_userDataActive.value?.userPass ?: "")
            _image.postValue(_userDataActive.value?.userPicture ?: "")
            _loadScreen.postValue(false)
            Log.d("DATA", "UserDataActive.value = ${userDataActive.value}")
        }
    }

    suspend fun onDataChange(name: String, email: String, pass: String, image: String) {
        _name.value = name
        _email.value = email
        _pass.value = pass
        _image.value = image
        _saveEnabled.value =
            isValidName(name = name) && isValidEmail(email = email) && isValidPass(pass = pass)

    }

    private fun isValidName(name: String): Boolean = name.length >= 3

    private fun isValidPass(pass: String): Boolean =
        Pattern.matches("""^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}${'$'}""", pass)

    private suspend fun isValidEmail(email: String): Boolean =
        getUserDataEmail(email = email).data?.userEmail.let { !it.isNullOrEmpty() }

    suspend fun onUpdateData(): Boolean {
        _isLoading.value = true

        if (_saveEnabled.value == true) _userDataActive.value?.let { data ->
            data.username = name.value!!
            data.userEmail = email.value!!
            data.userPass = pass.value!!
            data.userPicture = image.value!!

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

    suspend fun getUserDataId(id: String): Resource<UserData> =
        repository.getUserDataId(id = id)

    private suspend fun getUserDataEmail(email: String): Resource<UserData> =
        repository.getUserDataEmail(email = email)

    private fun updateUserData(userData: UserData) = viewModelScope.launch {
        repository.updateUserData(userData = userData)
        stateLogin.refreshData(userData = userData)
    }

    fun deleteUserData(userData: UserData) = viewModelScope.launch {
        repository.deleteUserData(userData = userData)
    }


    /**
     * stateLogin
     */

    fun logOut() = stateLogin.logOut()

    private fun getUserActive(): UserData? =
        stateLogin.userDataActive
}