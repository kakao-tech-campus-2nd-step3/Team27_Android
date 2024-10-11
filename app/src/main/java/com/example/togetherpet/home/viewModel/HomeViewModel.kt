package com.example.togetherpet.home.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.testData.entity.Missing
import com.example.togetherpet.testData.entity.User
import com.example.togetherpet.testData.repository.MissingRepository
import com.example.togetherpet.testData.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val missingRepository: MissingRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    //실종 동물 데이터
    private val _missingPets = MutableStateFlow<List<Missing>>(emptyList())
    val missingPets: StateFlow<List<Missing>> get() = _missingPets.asStateFlow()

    //유저&pet 데이터
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> get() = _user.asStateFlow()

    //Data Load 여부
    private val _isDataLoaded = MutableStateFlow(false)
    val isDataLoaded: StateFlow<Boolean> get() = _isDataLoaded

    fun loadData() {
        viewModelScope.launch {
            val missingJob = async { loadMissingPetData() }
            val userJob = async { loadUserData() }

            // 데이터를 불러오는 작업이 모두 끝난 뒤 flag 값 true로 설정
            awaitAll(missingJob, userJob)
            _isDataLoaded.value = true
        }
    }

    private suspend fun loadMissingPetData() {
        val pets = missingRepository.getAllMissingPets()
        _missingPets.emit(pets)
    }

    private suspend fun loadUserData() {
        val user = userRepository.getUserById(1)
        _user.emit(user)
    }
}