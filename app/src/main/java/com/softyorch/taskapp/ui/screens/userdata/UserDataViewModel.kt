package com.softyorch.taskapp.ui.screens.userdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import com.softyorch.taskapp.domain.userdataUseCase.UserDataUseCases
import com.softyorch.taskapp.ui.errors.ErrorInterface
import com.softyorch.taskapp.ui.errors.ErrorUserInput
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDataViewModel @Inject constructor(
    private val datastore: DatastoreUseCases,
    private val userDataUseCases: UserDataUseCases
) : ViewModel(), ErrorInterface {
    private val _userDataEntityActive = MutableLiveData<UserDataEntity>()
    val userDataEntityActive: LiveData<UserDataEntity> = _userDataEntityActive

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

    private val _errorEmailExists = MutableLiveData<Boolean>()
    val errorEmailExists: LiveData<Boolean> = _errorEmailExists

    private val _errorPass = MutableLiveData<Boolean>()
    val errorPass: LiveData<Boolean> = _errorPass

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _errorLoadData = MutableLiveData<Boolean>()
    val errorLoadData: LiveData<Boolean> = _errorLoadData

    private val _foundError = MutableLiveData<Boolean>()

/*    private val _mainImage: MutableLiveData<String?> =
        MutableLiveData<String?>(newImageGallery.value)
    val mainImage: LiveData<String?> = _mainImage*/

    init {
        _isLoading.value = true
        loadUserData()
    }

    fun onDataInputChange(name: String, email: String, pass: String) {
        _name.value = name
        _email.value = email
        _pass.value = pass
        _saveEnabled.value = true
        if (_foundError.value == true) {
            setErrorsData(error = withOutErrors(name = name, email = email, pass = pass))
        }
    }

    fun onImageInputChange(image: String) {
        _savingImage.value = true
        _isLoading.value = true
        viewModelScope.launch {
            _image.value = image
            _saveEnabled.value = true
        }.let {
            _isLoading.postValue(false)
        }
    }

    fun resetSavingImage() {
        _savingImage.value = false
    }

    fun onUpdateDataSend(
        name: String, email: String, pass: String, image: String
    ) {
        _isLoading.value = true
        withOutErrors(name = name, email = email, pass = pass).let { error ->
            setErrorsData(error = error)
            if (!error.error) {
                _userDataEntityActive.value?.let { user ->
                    user.username = name
                    user.userEmail = email
                    user.userPass = pass
                    user.userPicture = image
                }
                viewModelScope.launch(Dispatchers.IO) {
                    userDataEntityActive.value?.let { userData ->
                        updateUserData(userDataEntity = userData)
                        updateUserDataDatastore(userDataEntity = userData)
                    }
                }
            }
            _saveEnabled.postValue(false)
        }
    }

    private fun setErrorsData(error: ErrorUserInput.Error) {
        if (_foundError.value != true) _foundError.postValue(true)
        _errorName.postValue(error.name)
        _errorEmail.postValue(error.email)
        _errorEmailExists.postValue(error.emailExists)
        _errorPass.postValue(error.pass)
        _error.postValue(error.error)
    }

    fun resetErrorLoadData() {
        _errorLoadData.value = false
    }

    /**
     * data
     */

    private suspend fun getUserDataEmail(email: String): UserDataEntity? =
        userDataUseCases.getUserEmailExist(email = email)

    private suspend fun updateUserData(userDataEntity: UserDataEntity) =
        userDataUseCases.updateUser(userDataEntity = userDataEntity)

    /**
     * datastore
     */

    fun logOut() = viewModelScope.launch(Dispatchers.IO) { datastore.deleteData() }

    private fun loadUserData() = viewModelScope.launch(Dispatchers.IO) {
        datastore.getData().let { resource ->
            if (resource != null) {
                resource.flowOn(Dispatchers.IO).collect { data ->
                    /**REVISAR ESTO, SI REGRESA UN NULL NO PASARÁ NADA*/
                    getUserDataEmail(email = data.userEmail)?.let { user ->
                        _userDataEntityActive.postValue(user)
                        _name.postValue(user.username)
                        _email.postValue(user.userEmail)
                        _pass.postValue(user.userPass)
                        _image.postValue(user.userPicture)
                        _isLoading.postValue(false)
                    }
                }
            } else {
                //TODO
            }
        }
    }

    private suspend fun updateUserDataDatastore(userDataEntity: UserDataEntity) =
        datastore.saveData(userDataEntity = userDataEntity)
}