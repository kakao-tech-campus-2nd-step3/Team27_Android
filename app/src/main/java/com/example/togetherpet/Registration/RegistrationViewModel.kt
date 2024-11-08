package com.example.togetherpet.Registration

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.togetherpet.DataStoreRepository
import com.example.togetherpet.data.dto.PetRegisterDTO
import com.example.togetherpet.data.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val registerRepository: RegisterRepository,
    private val dataStoreRepository: DataStoreRepository
) :
    ViewModel() {

    // 필요한가?
    private val _petName = MutableStateFlow<String>(" ")
    private val _petAge = MutableStateFlow<Long>(0)
    private val _petSpecies = MutableStateFlow<String>(" ")
    private val _neutering = MutableStateFlow<Boolean>(false)
    private val _residence = MutableStateFlow<String>(" ")
    private val _petFeature = MutableStateFlow<String>(" ")
    private val _userName = MutableStateFlow<String>(" ")

    // todo : 이미지 타입 미정
    private val _petImage = MutableStateFlow<Uri>(Uri.EMPTY)
    val petName: StateFlow<String> get() = _petName.asStateFlow()
    val petAge: StateFlow<Long> get() = _petAge.asStateFlow()
    val petSpecies: StateFlow<String> get() = _petSpecies.asStateFlow()
    val neutering: StateFlow<Boolean> get() = _neutering.asStateFlow()
    val residence: StateFlow<String> get() = _residence.asStateFlow()
    val petFeature: StateFlow<String> get() = _petFeature.asStateFlow()
    val petImage: StateFlow<Uri> get() = _petImage.asStateFlow()
    val userName: StateFlow<String> get() = _userName.asStateFlow()

    fun setPetName(name: String) {
        _petName.value = name
        viewModelScope.launch {
            dataStoreRepository.savePetName(name)
        }
    }

    fun setPetAge(petAge: Long) {
        _petAge.value = petAge
        viewModelScope.launch {
            dataStoreRepository.savePetBirth(petAge)
        }
    }

    fun setPetSpecies(petSpecies: String) {
        _petSpecies.value = petSpecies
    }

    fun setNeutering(neutering: Boolean) {
        _neutering.value = neutering
        viewModelScope.launch {
            dataStoreRepository.savePetNeutral(neutering)
        }
    }

    fun setPetFeature(petFeature: String) {
        _petFeature.value = petFeature
    }

    fun setPetImage(petImage: Uri) {
        _petImage.value = petImage
        viewModelScope.launch {
            dataStoreRepository.savePetImgUri(petImage.toString())
        }
    }

    fun setUserName(userName: String) {
        _userName.value = userName
        viewModelScope.launch {
            dataStoreRepository.saveUserName(userName)
        }
    }

    fun registerUserAndPet(file: File) {
        viewModelScope.launch {
            Log.d("testt", mapToRegisterDTO().toString())
            registerRepository.registerUserAndPet(
                mapToRegisterDTO(),
                file,
                _userName.value
            )
        }
    }

    fun mapToRegisterDTO() = PetRegisterDTO(
        _petName.value,
        _petAge.value,
        _petSpecies.value,
        _neutering.value,
        _petFeature.value
    )
}
