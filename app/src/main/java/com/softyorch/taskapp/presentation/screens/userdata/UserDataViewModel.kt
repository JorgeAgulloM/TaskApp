package com.softyorch.taskapp.presentation.screens.userdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.DatastoreRepository
import com.softyorch.taskapp.domain.repository.UserDataRepository
import com.softyorch.taskapp.utils.StateLogin
import com.softyorch.taskapp.utils.REGEX_PASSWORD
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val stateLogin: StateLogin,
    private val datastore: DatastoreRepository
) : ViewModel() {
    private val _userDataActive = MutableLiveData<UserData>()
    val userDataActive: LiveData<UserData> = _userDataActive

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

    private val _savingImage = MutableLiveData<Boolean>()
    val savingImage: LiveData<Boolean> = _savingImage

/*    private val _mainImage: MutableLiveData<String?> =
        MutableLiveData<String?>(newImageGallery.value)
    val mainImage: LiveData<String?> = _mainImage*/

    init {
        _isLoading.value = true
        viewModelScope.launch { loadData() }
    }

    private suspend fun loadData() {
        val result = getUserData()
        userDataActive.value.let { user ->
            _image.postValue(user?.userPicture ?: emptyString)
            _name.postValue(user?.username ?: emptyString)
            _email.postValue(user?.userEmail ?: emptyString)
            _pass.postValue(user?.userPass ?: emptyString)
        }

        result.join()
        _isLoading.postValue(false)

        /*getUserActiveSharedPreferences().let { data ->
            viewModelScope.launch(Dispatchers.IO) {
                data?.userEmail.let { email ->
                    repository.getUserDataEmail(email = email!!).let { user ->
                        _userDataActive.postValue(user.data)
                        _image.postValue(user.data?.userPicture ?: "")
                        _name.postValue(user.data?.username ?: "")
                        _email.postValue(user.data?.userEmail ?: "")
                        _pass.postValue(user.data?.userPass ?: "")
                    }
                }
            }
        }.let {
            it.join()
            _isLoading.postValue(false)
        }*/
    }

    suspend fun onDataChange(name: String, email: String, pass: String) {
        _name.value = name
        _email.value = email
        _pass.value = pass
        _saveEnabled.value = isValidName(name = name) &&
                isValidEmail(email = email) &&
                isValidPass(pass = pass)
    }

    fun onImageChange(image: String) {
        _savingImage.value = true
        _isLoading.value = true
        viewModelScope.launch {
            _image.value = image
            _saveEnabled.value = true
        }.let {
            _isLoading.postValue(false)
        }
    }

    fun reloadImage(image: String) {
        viewModelScope.launch {
            _savingImage.value = true
            _image.value = ""
            delay(500)
            _image.value = image
            _savingImage.value = false
        }
    }

    fun resetSavingImage() {
        _savingImage.value = false
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
            data.userPicture = image.value!!
            viewModelScope.launch {
                updateUserData(userData = data)
            }.let {
                it.join()
                _isLoading.value = false
                _saveEnabled.postValue(false)
                return true
            }
        }

        return false
    }


    /**
     * data
     */

    private suspend fun getUserDataEmail(email: String): Resource<UserData> =
        repository.getUserDataEmail(email = email)

    private suspend fun updateUserData(userData: UserData) {
        viewModelScope.launch {
            repository.updateUserData(userData = userData)
        }.let {
            it.join()
            datastore.saveData(userData = userData)
            //stateLogin.refreshData(userData = userData)
        }

    }

/*    fun deleteUserData(userData: UserData) = viewModelScope.launch {
        repository.deleteUserData(userData = userData)
    }*/


    /**
     * stateLogin
     */

    fun logOut() = viewModelScope.launch(Dispatchers.IO) { datastore.deleteData() }//stateLogin.logOut()

    private suspend fun getUserData() = viewModelScope.launch(Dispatchers.IO) {
        datastore.getData().collect { userData ->
            _userDataActive.postValue(userData)
        }
    }

    /*private fun getUserActiveSharedPreferences(): UserData? =
        stateLogin.userDataActive*/
}