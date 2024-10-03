package com.example.togetherpet.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.testData.entity.Missing
import com.example.togetherpet.testData.entity.User
import com.example.togetherpet.testData.repository.MissingRepository
import com.example.togetherpet.testData.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val missingRepository: MissingRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _missingPets = MutableLiveData<List<Missing>>()
    val missingPets: LiveData<List<Missing>> get() = _missingPets

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user


    fun loadData(){
        loadMissingPetData()
        loadUserData()
    }

    private fun loadMissingPetData() {
        viewModelScope.launch {
            _missingPets.value = missingRepository.getAllMissingPets()
        }
    }

    private fun loadUserData() {
        viewModelScope.launch {
            _user.value = userRepository.getUserById(0)
        }
    }
}