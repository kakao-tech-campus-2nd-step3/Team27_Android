package com.jnu.togetherpet.ui.viewmodel.walking

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jnu.togetherpet.data.entity.WalkingRecord
import com.jnu.togetherpet.data.repository.UserRepository
import com.jnu.togetherpet.data.repository.WalkingRepository
import com.kakao.vectormap.LatLng
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class WalkingPetRecordViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WalkingPetRecordViewModel
    private val walkingRepository = mockk<WalkingRepository>(relaxed = true)
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { userRepository.getUserData() } returns mockk() {
            every { petName } returns "뽀삐"
        }
        viewModel = WalkingPetRecordViewModel(walkingRepository, userRepository)
    }

    @Test
    fun `반려동물 이름 설정 테스트`() = runTest {
        coEvery { userRepository.getUserData() } returns mockk {
            every { petName } returns "빠삐"
        }
        viewModel = WalkingPetRecordViewModel(walkingRepository, userRepository)
        assertEquals("빠삐", viewModel.petName.value)
    }

    @Test
    fun `로컬에서 기록 가져오기 테스트`() = runTest {
        val date = LocalDate.now()
        val recordList = arrayListOf(
            WalkingRecord(
                distance = 1500L,
                time = 4000L,
                calories = 60L,
                route = arrayListOf(LatLng.from(0.0, 0.0)),
                startTime = LocalDateTime.now(),
                endTime = LocalDateTime.now(),
                date = date
            )
        )
        coEvery { walkingRepository.getWalkingDataWithDateFromLocal(date) } returns recordList

        viewModel.getRecordToLocal(date)

        assertEquals(recordList, viewModel.arrayRecord.value)
        assertEquals(1500L, viewModel.allDistance.value)
        assertEquals(4000L, viewModel.allTime.value)
    }

}