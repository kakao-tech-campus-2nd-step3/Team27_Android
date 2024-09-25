package com.example.togetherpet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(private val tokenRepository: TokenRepository) :
    ViewModel() {
    fun postTokenToServer(token : String){
        viewModelScope.launch {
            tokenRepository.postToken(token)
        }
    }
}