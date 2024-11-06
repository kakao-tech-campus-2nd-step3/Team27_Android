package com.example.togetherpet

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.togetherpet.data.repository.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(private val tokenRepository : TokenRepository) : ViewModel() {
    private val _userLoginState = MutableStateFlow<Boolean>(false)
    val userLoginState : StateFlow<Boolean> get() = _userLoginState.asStateFlow()

    fun isLoggedIn(){
        if(tokenRepository.hasToken()) _userLoginState.value = true
        Log.d("testt", "token : ${_userLoginState.value}")
    }

}