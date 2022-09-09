package com.softyorch.taskapp.presentation.screens.userdata

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.data.Resource
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.DatastoreRepository
import com.softyorch.taskapp.domain.repository.UserDataRepository
import com.softyorch.taskapp.utils.REGEX_PASSWORD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val repository: UserDataRepository,
    private val datastore: DatastoreRepository
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

    private val _savingImage = MutableLiveData<Boolean>()
    val savingImage: LiveData<Boolean> = _savingImage

    private val _errorName = MutableLiveData<Boolean>()
    val errorName: LiveData<Boolean> = _errorName

    private val _errorEmail = MutableLiveData<Boolean>()
    val errorEmail: LiveData<Boolean> = _errorEmail

    private val _errorPass = MutableLiveData<Boolean>()
    val errorPass: LiveData<Boolean> = _errorPass

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _foundError = MutableLiveData<Boolean>()

/*    private val _mainImage: MutableLiveData<String?> =
        MutableLiveData<String?>(newImageGallery.value)
    val mainImage: LiveData<String?> = _mainImage*/

    init {
        _isLoading.value = true
        loadUserData()
    }

    fun onDataChange(name: String, email: String, pass: String) {
        _name.value = name
        _email.value = email
        _pass.value = pass
        _saveEnabled.value = true
        if (_foundError.value == true) {
            isValidEmail(email = email)
            _error.value =
                (!isValidName(name = name) || errorEmail.value == true || !isValidPass(pass = pass))
        }
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

    /*fun reloadImage(image: String) {
        viewModelScope.launch {
            _savingImage.value = true
            _image.value = ""
            delay(500)
            _image.value = image
            _savingImage.value = false
        }
    }*/

    fun resetSavingImage() {
        _savingImage.value = false
    }

    fun onUpdateData(
        name: String, email: String, pass: String, image: String
    ) {
        _isLoading.value = true

        viewModelScope.launch {
            withOutErrors(name = name, email = email, pass = pass).let { error ->
                if (_foundError.value != true) _foundError.postValue(error)
                _error.postValue(error)
                if (!error && _saveEnabled.value == true) {
                    _userDataActive.value?.let { data ->
                        data.username = name
                        data.userEmail = email
                        data.userPass = pass
                        data.userPicture = image
                        viewModelScope.launch(Dispatchers.IO) {
                            datastore.saveData(userData = data)
                            repository.updateUserData(userData = data)

                            _isLoading.postValue(false)
                            _saveEnabled.postValue(false)
                        }
                    }
                } else {
                    _isLoading.postValue(false)
                }
            }
        }
    }

    /**
     * Errors control
     */

    private fun withOutErrors(name: String, email: String, pass: String): Boolean {
        isValidEmail(email = email)
        return (!isValidName(name = name) || errorEmail.value == true || !isValidPass(pass = pass))
    }

    private fun isValidName(name: String): Boolean =
        (name.length >= 3).also { _errorName.value = !it }

    private fun isValidPass(pass: String): Boolean =
        Pattern.matches(REGEX_PASSWORD, pass).also { _errorPass.value = !it }

    private fun isValidEmail(email: String) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            viewModelScope.launch(Dispatchers.IO) {
                datastore.getData().collect { datastore ->
                    if (email != datastore.userEmail) {
                        getUserDataEmail(email = email).data?.userEmail.let { email ->
                            _errorEmail.postValue(!email.isNullOrEmpty())
                        }
                    } else {
                        _errorEmail.postValue(false)
                    }
                }
            }
        } else _errorEmail.value = true
    }

    /**
     * data
     */

    private suspend fun getUserDataEmail(email: String): Resource<UserData> =
        repository.getUserDataEmail(email = email)

    /**
     * datastore
     */

    fun logOut() = viewModelScope.launch(Dispatchers.IO) { datastore.deleteData() }

    private fun loadUserData() = viewModelScope.launch(Dispatchers.IO) {
        datastore.getData().collect { userData ->
            getUserDataEmail(email = userData.userEmail).data?.let { user ->
                _userDataActive.postValue(user)
                _name.postValue(user.username)
                _email.postValue(user.userEmail)
                _pass.postValue(user.userPass)
                _image.postValue(user.userPicture)
                _isLoading.postValue(false)
            }
        }
    }

}