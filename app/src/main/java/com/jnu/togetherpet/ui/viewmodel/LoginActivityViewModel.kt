package com.jnu.togetherpet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnu.togetherpet.data.repository.LoginRepository
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