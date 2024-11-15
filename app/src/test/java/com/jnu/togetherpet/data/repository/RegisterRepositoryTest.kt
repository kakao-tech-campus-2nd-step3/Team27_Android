package com.jnu.togetherpet.data.repository

import android.util.Log
import com.jnu.togetherpet.data.datasource.RegisterSource
import com.jnu.togetherpet.data.dto.PetRegisterDTO
import com.jnu.togetherpet.exception.APIException
import io.mockk.coEvery
import io.mockk.coVerify
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
import org.junit.rules.TestRule
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jnu.togetherpet.exception.ErrorResponse
import io.mockk.coVerifyOrder
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.verifySequence
import java.io.File

class RegisterRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val registerSource = mockk<RegisterSource>(relaxed = true)
    private val tokenRepository = mockk<TokenRepository>(relaxed = true)
    private lateinit var registerRepository: RegisterRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        registerRepository = RegisterRepository(registerSource, tokenRepository)
        coEvery { tokenRepository.getTokenOrThrow() } returns "token bearer"
    }

    @Test
    fun `사용자와 반려동물 등록 성공 테스트`() = runTest {
        val petRegisterDTO = PetRegisterDTO("뽀삐", 7L, "말티즈", true, "낯을 안가림")
        val petImage = mockk<File>(relaxed = true)
        val userName = "짱구"

        registerRepository.registerUserAndPet(petRegisterDTO, petImage, userName)

        coVerify {
            registerSource.registerUserAndPet(
                "token bearer",
                petRegisterDTO,
                petImage,
                userName
            )
        }
    }

    @Test
    fun `APIException 조건 처리 테스트`() = runTest {
        val petRegisterDTO = PetRegisterDTO("뽀삐", 7L, "고양이", true, "낯을 안가림")
        val petImage = mockk<File>(relaxed = true)
        val userName = "짱구"

        coEvery {
            registerSource.registerUserAndPet(any(), any(), any(),any())
        } throws APIException(ErrorResponse(-20401, "잘못된 종 입력."))

        val exception = runCatching {
            registerRepository.registerUserAndPet(petRegisterDTO, petImage, userName)
        }.exceptionOrNull() as? APIException

        assertNotNull(exception)
        assertEquals(-20401, exception?.errorResponse?.code)
        assertEquals("잘못된 종 입력.", exception?.errorResponse?.message)
    }
}