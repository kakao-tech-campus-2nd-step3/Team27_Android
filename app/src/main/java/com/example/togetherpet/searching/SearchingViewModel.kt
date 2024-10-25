package com.example.togetherpet.searching

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.testData.entity.Missing
import com.example.togetherpet.testData.entity.User
import com.example.togetherpet.testData.repository.MissingRepository
import com.example.togetherpet.testData.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchingViewModel @Inject constructor(
    private val missingRepository: MissingRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _petName = MutableStateFlow<String?>(null)
    val petName: StateFlow<String?> get() = _petName.asStateFlow()

    private val _selectedBtn = MutableStateFlow<String?>(null)
    val selectedBtn: StateFlow<String?> get() = _selectedBtn.asStateFlow()

    //실종 동물 데이터
    private val _missingPets = MutableStateFlow<List<Missing>>(emptyList())
    val missingPets: StateFlow<List<Missing>> get() = _missingPets.asStateFlow()

    private val _selectedPet = MutableStateFlow<Missing>(Missing(0, "", 0, "", "", 0.0, 0.0))
    val selectedPet: StateFlow<Missing> get() = _selectedPet.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            loadMissingPetData()
            val userJob = async { loadUserData() }
            val user = userJob.await()

            _petName.value = user?.petName ?: "오류"
        }
    }

    private suspend fun loadMissingPetData() {
        val pets = missingRepository.getAllMissingPets()
        _missingPets.emit(pets)
    }

    private suspend fun loadUserData(): User? {
        return userRepository.getUserById(1)
    }

    fun pushBtn(btn: String) {
        _selectedBtn.value = btn
    }

    fun fetchMissingPetById(id: Int) {
        viewModelScope.launch {
            val selectedPetData = missingRepository.getMissingPetById(id)
            if (selectedPetData != null) {
                _selectedPet.emit(selectedPetData)
            }
        }
    }
}