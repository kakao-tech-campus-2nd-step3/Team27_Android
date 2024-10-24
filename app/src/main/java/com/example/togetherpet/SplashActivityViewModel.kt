package com.example.togetherpet

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor() : ViewModel() {
    private val _userLoginState = MutableStateFlow<Boolean>(false)
    val userLoginState : StateFlow<Boolean> get() = _userLoginState.asStateFlow()

    fun isLoggedIn(){
        //Todo : 수정
        _userLoginState.value = true
    }

}