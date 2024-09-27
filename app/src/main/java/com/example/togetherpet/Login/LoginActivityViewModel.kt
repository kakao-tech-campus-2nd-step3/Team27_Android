package com.example.togetherpet.Login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.TokenRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(private val tokenRepository: TokenRepository) :
    ViewModel() {
    fun postTokenToServer(token : String){
        viewModelScope.launch {
            Log.d("testt", "token : ${token}, view")
            tokenRepository.postToken(token)
        }
    }
}