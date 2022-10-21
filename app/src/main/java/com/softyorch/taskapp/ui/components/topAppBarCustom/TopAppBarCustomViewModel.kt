package com.softyorch.taskapp.ui.components.topAppBarCustom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softyorch.taskapp.data.database.userdata.UserDataEntity
import com.softyorch.taskapp.domain.datastoreUseCase.DatastoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TopAppBarCustomViewModel @Inject constructor(
    private val datastore: DatastoreUseCases
) : ViewModel() {
    private val _userDataEntity = MutableLiveData<UserDataEntity>()
    val userDataEntity: LiveData<UserDataEntity> = _userDataEntity

    private val _imageUser = MutableLiveData<String>()
    val imageUser: LiveData<String> = _imageUser

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    init {
        getUserData()
    }

    private fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            getData().collect { user ->
                _userDataEntity.postValue(user).let {
                    _imageUser.postValue(user.userPicture)
                    _userName.postValue(user.username)
                }
            }
        }
    }

    /** data */

    private fun getData() = datastore.getData()

}