package com.example.togetherpet.Registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.PetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val petRepository: PetRepository) :
    ViewModel() {

    // 필요한가?
    private val _petName = MutableStateFlow<String>(" ")
    private val _petAge = MutableStateFlow<Int>(0)
    private val _petSpecies = MutableStateFlow<String>(" ")
    private val _neutering = MutableStateFlow<Boolean>(false)
    private val _residence = MutableStateFlow<String>(" ")
    private val _petFeature = MutableStateFlow<String>(" ")

    // todo : 이미지 타입 미정
    private val _petImage = MutableStateFlow<String>(" ")
    val petName: StateFlow<String> get() = _petName.asStateFlow()
    val petAge: StateFlow<Int> get() = _petAge.asStateFlow()
    val petSpecies: StateFlow<String> get() = _petSpecies.asStateFlow()
    val neutering: StateFlow<Boolean> get() = _neutering.asStateFlow()
    val residence: StateFlow<String> get() = _residence.asStateFlow()
    val petFeature: StateFlow<String> get() = _petFeature.asStateFlow()
    val petImage: StateFlow<String> get() = _petImage.asStateFlow()


    fun setPetName(name: String) {
        _petName.value = name
    }

    fun setPetAge(petAge: Int) {
        _petAge.value = petAge
    }

    fun setPetSpecies(petSpecies: String) {
        _petSpecies.value = petSpecies
    }

    fun setNeutering(neutering: Boolean) {
        _neutering.value = neutering
    }

    fun setPetFeature(petFeature: String) {
        _petFeature.value = petFeature
    }

    fun setPetImage(petImage: String) {
        _petImage.value = petImage
    }


    fun sendPetInfoToServer() {
        viewModelScope.launch {
            val code = petRepository.postPetInfo(
                _petName.value,
                _petAge.value,
                _petSpecies.value,
                _neutering.value,
                _petFeature.value,
                _petImage.value
            ).code
        }
    }

    fun sendUserInfoToServer() {

    }


}