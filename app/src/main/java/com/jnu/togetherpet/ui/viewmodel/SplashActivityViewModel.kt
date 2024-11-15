package com.jnu.togetherpet.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnu.togetherpet.data.repository.LoginRepository
import com.jnu.togetherpet.data.repository.TokenRepository
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashActivityViewModel @Inject constructor(private val tokenRepository : TokenRepository, private val loginRepository: LoginRepository) : ViewModel() {
    private val _userLoginState = MutableStateFlow(false)
    val userLoginState: StateFlow<Boolean> get() = _userLoginState.asStateFlow()

    fun isLoggedIn() {
        if (tokenRepository.hasToken()) {
            _userLoginState.value = true
            getNewToken()
        }
        Log.d("testt", "token : ${_userLoginState.value}")
    }

    private fun getNewToken() {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e("testt", "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    "testt", "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
                viewModelScope.launch(Dispatchers.IO){
                    loginRepository.login(user.kakaoAccount?.email.toString())

                }
            }
        }
    }
}