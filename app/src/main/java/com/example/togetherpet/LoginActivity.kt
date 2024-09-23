package com.example.togetherpet

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient

class LoginActivity : AppCompatActivity() {
    val TAG = "testt"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

        findViewById<ImageView>(R.id.kakao_login_bar).setOnClickListener {
            if (AuthApiClient.instance.hasToken()) {
                UserApiClient.instance.accessTokenInfo { _, error ->
                    if (error != null) {
                        if (error is KakaoSdkError && error.isInvalidTokenError() == true) {
                            //로그인 필요
                            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                                    if (error != null) {
                                        Log.e(TAG, "로그인 실패!", error)

                                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                            return@loginWithKakaoTalk
                                        }
                                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)

                                    } else if (token != null) {
                                        Log.i(TAG, "톡으로 로그인 : ${token.accessToken}")
                                    }
                                }
                            } else {
                                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                            }
                        }
                        else {
                            //기타 에러

                        }
                    }
                    else {
                        Log.d(TAG, "토큰 검증")
                        //토큰 유효성 체크 성공(필요 시 토큰 갱신됨)
                        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                            if (error != null) {
                                Log.e(TAG, "토큰 정보 보기 실패", error)
                            }
                            else if (tokenInfo != null) {
                                Log.i(TAG, "토큰 정보 보기 성공" +
                                        "\n회원번호: ${tokenInfo.id}" +
                                        "\n만료시간: ${tokenInfo.expiresIn} 초")
                            }
                        }
                    }
                }
            }
        }
    }

}