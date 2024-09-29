package com.example.togetherpet.testData.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.testData.entity.Missing
import com.example.togetherpet.testData.repository.MissingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MissingViewModel @Inject constructor(private val missingRepository: MissingRepository) :
    ViewModel() {
    private val _missingPet = MutableLiveData<List<Missing>>()
    val missingPet: LiveData<List<Missing>> get() = _missingPet

    fun addDummyMissingPet() {
        val dummyPets = listOf(
            Missing(missingPetName = "꼬맹이", missingDate = 7, missingPlace = "전남대학교 대운동장"),
            Missing(missingPetName = "뽀삐", missingDate = 16, missingPlace = "전남대학교 대운동장로 77")
        )

        viewModelScope.launch {
            for (dummyPet in dummyPets) {
                missingRepository.insertMissingPet(dummyPet)
            }
            _missingPet.value = missingRepository.getAllMissingPets()
        }
    }
}