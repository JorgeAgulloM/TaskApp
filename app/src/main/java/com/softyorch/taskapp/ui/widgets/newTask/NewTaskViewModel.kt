package com.softyorch.taskapp.ui.widgets.newTask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.softyorch.taskapp.ui.errors.ErrorInterface
import com.softyorch.taskapp.ui.errors.ErrorUserInput
import com.softyorch.taskapp.utils.emptyString
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewTaskViewModel @Inject constructor() : ViewModel(), ErrorInterface {
    private val _title = MutableLiveData<String>()
    val title: LiveData<String> = _title

    private val _description = MutableLiveData<String>()
    val description: LiveData<String> = _description

    private val _saveTaskEnabled = MutableLiveData<Boolean>()
    val saveTaskEnable: LiveData<Boolean> = _saveTaskEnabled

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _titleDeedCounter = MutableLiveData<Int>(0)
    val titleDeedCounter: LiveData<Int> = _titleDeedCounter
    val limitCharTittle = 30

    private val _errorTittle = MutableLiveData<Boolean>()
    val errorTittle: LiveData<Boolean> = _errorTittle

    private val _errorDescription = MutableLiveData<Boolean>()
    val errorDescription: LiveData<Boolean> = _errorDescription

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _error

    private val _foundError = MutableLiveData<Boolean>()

    fun onTextFieldInputChanged(title: String, description: String): Boolean {
        _title.also { it.value = if (title.length <= limitCharTittle) title else it.value }
        _description.value = description
        _saveTaskEnabled.value = true
        _titleDeedCounter.value = title.length
        if (_foundError.value == true) {
            withOutErrorsNewTask(title = title, description = description).let { error ->
                setErrors(error = error)
                return error.error
            }
        } else return false
    }

    fun onTextInputSend(title: String, description: String): Boolean {
        _isLoading.value = true
        withOutErrorsNewTask(title = title, description = description).let { error ->
            setErrors(error = error)
            _isLoading.value = false
            return error.error
        }
    }

    private fun setErrors(error: ErrorUserInput.Error) {
        if (_foundError.value != true) _foundError.value = true
        _errorTittle.value = error.title
        _errorDescription.value = error.description
        _error.value = error.error
    }

    fun onResetValues() {
        _title.value = emptyString
        _description.value = emptyString
        _errorTittle.value = false
        _errorDescription.value = false
        _error.value = false
        _foundError.value = false
    }
}