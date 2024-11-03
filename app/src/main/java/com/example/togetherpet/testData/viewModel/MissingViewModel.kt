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
            Missing(missingPetName = "꼬맹이", missingDate = 7, missingPlace = "전남대학교 대운동장", missingPetImgUrl = "https://cdn.pixabay.com/photo/2020/03/31/20/23/cat-4989143_1280.jpg", missingLatitude = 35.17432851043129, missingLongitude = 126.9067283686867),
            Missing(missingPetName = "뽀삐", missingDate = 16, missingPlace = "전남대학교 풋살장", missingPetImgUrl = "https://www.antigenne.com/wp-content/uploads/2024/08/CAV-II-Ag.webp", missingLatitude = 34.77704292474046, missingLongitude = 127.70177930295718 )
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