package com.example.togetherpet

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegistrationViewModel : ViewModel() {
    private val _name = MutableStateFlow<String>(" ")
    private val _age = MutableStateFlow<String>(" ")
    private val _species = MutableStateFlow<String>(" ")
    private val _neutering = MutableStateFlow<Boolean>(false)
    val name : StateFlow<String> get() = _name.asStateFlow()
    val age : StateFlow<String> get() = _age.asStateFlow()
    val species : StateFlow<String> get() = _species.asStateFlow()
    val neutering : StateFlow<Boolean> get() = _neutering.asStateFlow()

    fun setName(name : String){

    }

    fun setAge(age : String){

    }

    fun setSpecies(species : String){

    }

    fun setNeutering(neutering : String){

    }



}