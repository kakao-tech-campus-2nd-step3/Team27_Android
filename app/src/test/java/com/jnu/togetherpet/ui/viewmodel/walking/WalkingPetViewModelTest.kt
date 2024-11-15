package com.jnu.togetherpet.ui.viewmodel.walking

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.jnu.togetherpet.data.repository.UserRepository
import com.jnu.togetherpet.data.repository.WalkingRepository
import com.jnu.togetherpet.ui.fragment.walking.WalkingPetFragment
import com.kakao.vectormap.LatLng
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.manipulation.Ordering

class WalkingPetViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: WalkingPetViewModel
    private val context = mockk<Context>(relaxed = true)
    private val fusedLocationProviderClient = mockk<FusedLocationProviderClient>(relaxed = true)
    private val walkingRepository = mockk<WalkingRepository>(relaxed = true)
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        coEvery { userRepository.getUserData() } returns mockk(relaxed = true) {
            every { petImageUri  } returns mockk<Uri>()
            every { petName  } returns "뽀삐"
        }
        viewModel = WalkingPetViewModel(context, fusedLocationProviderClient, walkingRepository, userRepository, mockk())

    }

    @Test
    fun `반려동물 이미지 설정 테스트`() = runTest {
        viewModel.setPetImage()
        assertEquals(userRepository.getUserData().petImageUri, viewModel.petImage.value)
    }

    @Test
    fun `반려동물 이름 설정 테스트`() = runTest {
        viewModel.setPetName()
        assertEquals("뽀삐", viewModel.petName.value)
    }

    @Test
    fun `산책 거리 계산 테스트`() {
        val loc1 = LatLng.from(34.753051290852135, 127.6758172363043)
        val loc2 = LatLng.from(34.749669571227535, 127.67578572034836)
        viewModel.calculateDistance(loc1, loc2)
        assertTrue(viewModel.distance.value == 376)
    }

    @Test
    fun `칼로리 계산 테스트`() {
        viewModel.calculateCalories()
        assertEquals(0, viewModel.calories.value) // 초기 거리 값이 0이므로 칼로리도 0
        viewModel.calculateDistance(
            LatLng.from(34.753051290852135, 127.6758172363043),
            LatLng.from(34.749669571227535, 127.67578572034836)
        ) // 산책 거리 추가
        viewModel.calculateCalories()
        assertTrue(viewModel.calories.value == 4)
    }

    @Test
    fun `타이머 시작 및 시간 경과 테스트`() {
        viewModel.setTimeBase()
        Thread.sleep(1000) // 1초 대기
        viewModel.timerStart()
        assertTrue(viewModel.time.value >= 1000)
    }

    @Test
    fun `최소 산책 시간 미만 여부 확인`() {
        viewModel.initValue()
        assertTrue(viewModel.isTimeUnderMinTime())
    }

    @Test
    fun `최소 산책 거리 미만 여부 확인`() {
        viewModel.initValue()
        assertTrue(viewModel.isDistanceUnderMinDistance())
    }

    @Test
    fun `산책 데이터 전송 테스트`() = runTest {
        viewModel.calculateDistance(
            LatLng.from(34.753051290852135, 127.6758172363043),
            LatLng.from(34.749669571227535, 127.67578572034836)
        )
        viewModel.calculateCalories()
        viewModel.timerStart()
        viewModel.sendWalkingData()

        coVerify { walkingRepository.sendWalkingDataToLocal(any(), any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `10m 미만의 산책 데이터 전송 테스트`() = runTest {
        viewModel.calculateDistance(
            LatLng.from(34.753051290852135, 127.6758172363043),
            LatLng.from(34.753051290852135, 127.6758172363046)
        )
        viewModel.calculateCalories()
        viewModel.timerStart()
        viewModel.sendWalkingData()

        coVerify(exactly = 0) { walkingRepository.sendWalkingDataToLocal(any(), any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `1분 미만의 산책 데이터 전송 테스트`() = runTest {
        viewModel.calculateDistance(
            LatLng.from(34.753051290852135, 127.6758172363043),
            LatLng.from(34.749669571227535, 127.67578572034836)
        )
        viewModel.calculateCalories()
        viewModel.setTimeBase()
        viewModel.timerStart()
        viewModel.sendWalkingData()

        coVerify(exactly = 0) { walkingRepository.sendWalkingDataToLocal(any(), any(), any(), any(), any(), any(), any()) }
    }
}