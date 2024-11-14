package com.jnu.togetherpet.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jnu.togetherpet.data.dto.PetRegisterDTO
import com.jnu.togetherpet.data.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
) : ViewModel() {
    fun registerUserAndPet(
        petRegisterDTO: PetRegisterDTO,
        petImage: File,
        userName: String
    ) {
        viewModelScope.launch {
            registerRepository.registerUserAndPet(
                petRegisterDTO,
                petImage,
                userName
            )
        }
    }
}