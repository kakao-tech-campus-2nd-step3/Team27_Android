package com.jnu.togetherpet.ui.viewmodel.report

import android.net.Uri
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jnu.togetherpet.data.dto.PetRegisterDTO
import com.jnu.togetherpet.data.repository.DataStoreRepository
import com.jnu.togetherpet.data.repository.RegisterRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import java.io.File

class RegistrationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegistrationViewModel
    private val mockRegisterRepository = mockk<RegisterRepository>(relaxed = true)
    private val mockDataStoreRepository = mockk<DataStoreRepository>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Dispatchers.Main을 가상의 TestDispatcher로 설정
        Dispatchers.setMain(testDispatcher)

        viewModel = RegistrationViewModel(mockRegisterRepository, mockDataStoreRepository)
        mockkStatic(android.util.Log::class)
        every { Log.d(any(), any()) } returns 0
    }

    @Test
    fun `반려동물 이름 설정이 제대로 이루어 지는지`() {
        val petName = "뽀삐"

        viewModel.setPetName(petName)

        assertEquals(petName, viewModel.petName.value)
    }

    @Test
    fun `반려동물 개월 수 입력이 제대로 이루어 지는지`() {
        val petAge = 5L

        viewModel.setPetAge(petAge)

        assertEquals(petAge, viewModel.petAge.value)
    }

    @Test
    fun `반려동물 종 입력이 제대로 이루어 지는지`() {
        val petSpecies = "말티즈"

        viewModel.setPetSpecies(petSpecies)

        assertEquals(petSpecies, viewModel.petSpecies.value)
    }

    @Test
    fun `반려동물 중성화 유무 입력이 제대로 이루어 지는지`() {
        val neutering = true

        viewModel.setNeutering(neutering)

        assertEquals(neutering, viewModel.neutering.value)
    }

    @Test
    fun `반려동물 특징 입력이 제대로 이루어 지는지`() {
        val petFeature = "특징"

        viewModel.setPetFeature(petFeature)

        assertEquals(petFeature, viewModel.petFeature.value)
    }

    @Test
    fun `반려동물 이미지 입력이 제대로 이루어 지는지`() {
        val uri = mockk<Uri>()

        viewModel.setPetImage(uri)

        assertEquals(uri, viewModel.petImage.value)
    }

    @Test
    fun `반려동물 닉네임 입력이 제대로 이루어 지는지`() {
        val userName = "짱구"

        viewModel.setUserName(userName)

        assertEquals(userName, viewModel.userName.value)
    }

    @Test
    fun `user 정보와 pet 정보를 등록 테스트`() {
        val file = mockk<File>(relaxed = true)
        val petName = "뽀삐"
        val petAge = 5L
        val petSpecies = "말티즈"
        val neutering = true
        val petFeature = "특징"
        val userName = "짱구"

        viewModel.setPetName(petName)
        viewModel.setPetAge(petAge)
        viewModel.setPetSpecies(petSpecies)
        viewModel.setNeutering(neutering)
        viewModel.setPetFeature(petFeature)
        viewModel.setUserName(userName)
        val dto = viewModel.mapToRegisterDTO()

        // When
        viewModel.registerUserAndPet(file)

        // Then
        coVerify {
            mockRegisterRepository.registerUserAndPet(
                dto,
                file,
                userName
            )
        }
    }

    @Test
    fun `반려동물 입력을 통신에 필요한 DTO로 매핑이 제대로 이루어 지는지`() {
        val petName = "뽀삐"
        val petAge = 5L
        val petSpecies = "말티즈"
        val neutering = true
        val petFeature = "특징"

        viewModel.setPetName(petName)
        viewModel.setPetAge(petAge)
        viewModel.setPetSpecies(petSpecies)
        viewModel.setNeutering(neutering)
        viewModel.setPetFeature(petFeature)

        // When
        val dto = viewModel.mapToRegisterDTO()

        // Then
        assertEquals(petName, dto.petNAme)
        assertEquals(petAge, dto.petBirthMonth)
        assertEquals(petSpecies, dto.petType)
        assertEquals(neutering, dto.isNeutering)
        assertEquals(petFeature, dto.petFeature)

    }
}