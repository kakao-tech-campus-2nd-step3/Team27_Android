package com.example.togetherpet.testData.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.testData.entity.User
import com.example.togetherpet.testData.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    suspend fun addDummyUser() {
        val dummyUser = User(
            userNickname = "전대용봉탕", petName = "꾸릉이",
            petImgUrl = "https://us.123rf.com/450wm/alexeitm/alexeitm1607/alexeitm160700009/59730361-active-dog-playing-and-running-with-a-colorful-ball.jpg?ver=6",
            todayWalkCount = 2,
            todayWalkDistance = 5.5,
            todayWalkTime = "0:40",
            avgWalkCount = 2,
            avgWalkDistance = 8.1,
            avgWalkTime = "1:06"
        )

        viewModelScope.launch {
            val existingUser = userRepository.getUserByName(dummyUser.userNickname)
            if (existingUser == null){
                userRepository.insertUser(dummyUser)
            }
            _user.value = userRepository.getUserById(1)
        }
    }
}