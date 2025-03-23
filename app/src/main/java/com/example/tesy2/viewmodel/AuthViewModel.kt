package com.example.tesy2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tesy2.data.models.AppUser
import com.example.tesy2.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _gender = MutableStateFlow("")
    val gender: StateFlow<String> = _gender

    private val _job = MutableStateFlow("")
    val job: StateFlow<String> = _job

    private val _location = MutableStateFlow("")
    val location: StateFlow<String> = _location

    private val _birthDate = MutableStateFlow("")
    val birthDate: StateFlow<String> = _birthDate

    private val _signUpSuccess = MutableStateFlow(false)
    val signUpSuccess: StateFlow<Boolean> = _signUpSuccess

    fun onEmailChange(value: String) { _email.value = value }
    fun onPasswordChange(value: String) { _password.value = value }
    fun onNameChange(value: String) { _name.value = value }
    fun onGenderChange(value: String) { _gender.value = value }
    fun onJobChange(value: String) { _job.value = value }
    fun onLocationChange(value: String) { _location.value = value }
    fun onBirthDateChange(value: String) { _birthDate.value = value }

    fun signUp() {
        viewModelScope.launch {
            println("📤 Appel de signUp() and auth view model")
            val user = AppUser(
                user_id = "", // sera remplacé dans le repo
                name = name.value,
                gender = gender.value,
                job = job.value,
                home_location = location.value,
                birth_date = birthDate.value
            )

            val success = repository.signUp(email.value, password.value, user)
            _signUpSuccess.value = success
        }
    }
}
