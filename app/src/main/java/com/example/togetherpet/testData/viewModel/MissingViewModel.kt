package com.example.togetherpet.testData.viewModel

import android.util.Log
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

    suspend fun addDummyMissingPet() {
        val dummyPets = listOf(
            Missing(missingPetName = "꼬맹이", missingDate = 7, missingPlace = "전남대학교 대운동장", missingPetImgUrl = "https://cdn.pixabay.com/photo/2020/03/31/20/23/cat-4989143_1280.jpg"),
            Missing(missingPetName = "뽀삐", missingDate = 16, missingPlace = "전남대학교 대운동장로 77", missingPetImgUrl = "https://www.antigenne.com/wp-content/uploads/2024/08/CAV-II-Ag.webp")
        )

        viewModelScope.launch {
            for (dummyPet in dummyPets) {
                val existingPet = missingRepository.getMissingPetByName(dummyPet.missingPetName)
                if(existingPet == null){
                    missingRepository.insertMissingPet(dummyPet)
                }
            }
            val insertedPets = missingRepository.getAllMissingPets()
            Log.d("yeong","삽입 ${insertedPets.size}, Data: $insertedPets")
        }
    }
}