package com.example.togetherpet.Login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {
    fun login(email: String) {
        viewModelScope.launch {
            loginRepository.login(email)
        }
    }
}