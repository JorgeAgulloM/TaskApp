package com.softyorch.taskapp.presentation.widgets.newTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.utils.emptyString
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

    private val _errorTittle = MutableLiveData<Boolean>()
    val errorTittle: LiveData<Boolean> = _errorTittle

    private val _errorDescription = MutableLiveData<Boolean>()
    val errorDescription: LiveData<Boolean> = _errorDescription

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _foundError = MutableLiveData<Boolean>()

    fun onTextFieldChanged(title: String, description: String) {
        _title.value = title
        _description.value = description
        _saveTaskEnabled.value = true
        if (_foundError.value == true) {
            _error.value = withOutErrors(title = title, description = description)
        }
    }

    fun withOutErrors(title: String, description: String): Boolean =
        (!isTittleValid(title = title) || !isDescriptionValid(description = description)).also { error ->
            if (_foundError.value != true) _foundError.postValue(error)
            _error.postValue(error)
        }

    private fun isTittleValid(title: String): Boolean =
        (title.length >= 3).also { _errorTittle.value = !it }

    private fun isDescriptionValid(description: String): Boolean =
        (description.length >= 3).also { _errorDescription.value = !it }

    fun onResetValues() {
        _title.value = emptyString
        _description.value = emptyString
        _errorTittle.value = false
        _errorDescription.value = false
        _error.value = false
        _foundError.value = false
    }
}