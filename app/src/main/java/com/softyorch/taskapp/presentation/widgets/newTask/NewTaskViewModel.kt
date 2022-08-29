package com.softyorch.taskapp.presentation.widgets.newTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor() : ViewModel() {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title
    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description
    private val _saveTaskEnabled = MutableLiveData<Boolean>()
    val saveTaskEnable: LiveData<Boolean> = _saveTaskEnabled

    fun onTextFieldChanged(title: String, description: String) {
        _title.value = title
        _description.value = description
        _saveTaskEnabled.value = isTittleValid(title = title) &&
                isDescriptionValid(description = description)
    }

    private fun isTittleValid(title: String): Boolean =
        title.length >= 3

    private fun isDescriptionValid(description: String): Boolean =
        description.length >= 3
}