package com.softyorch.taskapp.presentation.components.topAppBarCustom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.domain.model.UserData
import com.softyorch.taskapp.domain.repository.DatastoreRepository
import com.softyorch.taskapp.utils.StateLogin
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopAppBarCustomViewModel @Inject constructor(
    private val datastore: DatastoreRepository
) : ViewModel() {
    private val _userData = MutableLiveData<UserData>()
    val userData: LiveData<UserData> = _userData

    private val _imageUser = MutableLiveData<String>()
    val imageUser: LiveData<String> = _imageUser

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            datastore.getData().collect { userData ->
                _userData.postValue(userData).let {
                    _imageUser.postValue(userData.userPicture)
                    _userName.postValue(userData.username)
                }
            }
        }
    }
}