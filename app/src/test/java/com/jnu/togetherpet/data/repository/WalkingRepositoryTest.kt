package com.jnu.togetherpet.data.repository

import android.net.Uri
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jnu.togetherpet.data.datasource.WalkingLocalSource
import com.jnu.togetherpet.data.datasource.WalkingNetworkSource
import com.jnu.togetherpet.data.dto.LocationDTO
import com.jnu.togetherpet.data.dto.WalkingRequestDTO
import com.jnu.togetherpet.data.entity.WalkEntity
import com.jnu.togetherpet.ui.viewmodel.walking.WalkingPetViewModel
import com.kakao.vectormap.LatLng
import io.mockk.coEvery
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
import java.time.LocalDate
import java.time.LocalDateTime

class WalkingRepositoryTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var walkingRepository: WalkingRepository
    private val mockNetworkSource: WalkingNetworkSource = mockk(relaxed = true)
    private val mockTokenRepository: TokenRepository = mockk(relaxed = true)
    private val mockLocalSource: WalkingLocalSource = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    //배포 이슈로 인하여 Server통신 함수들은 사용 안함

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        walkingRepository = WalkingRepository(
            walkingNetworkSource = mockNetworkSource,
            tokenRepository = mockTokenRepository,
            walkingLocalSource = mockLocalSource
        )
    }

    @Test
    fun `로컬 데이터베이스로 데이터를 전송하는 테스트`() = runTest {
        val distance = 500
        val walkTime = 1000L
        val startTime = LocalDateTime.now()
        val endTime = startTime.plusMinutes(10)
        val locations = arrayListOf(
            LatLng.from(34.753051290852135, 127.6758172363043),
            LatLng.from(34.749669571227535, 127.67578572034836)
        )
        val calories = 50L
        val walkDay = LocalDate.now()

        // Act
        walkingRepository.sendWalkingDataToLocal(distance, walkTime, startTime, endTime, locations, calories, walkDay)

        // Assert
        coVerify {
            mockLocalSource.insertWalkingData(
                withArg<WalkEntity> {
                    assertEquals(distance.toLong(), it.walkDistance)
                    assertEquals(walkTime, it.walkTime)
                    assertEquals(startTime, it.walkStartTime)
                    assertEquals(endTime, it.walkEndTime)
                    assertEquals(calories, it.walkCalories)
                    assertEquals(walkDay, it.walkDay)
                    assertEquals(locations, it.locationList)
                }
            )
        }
    }

    @Test
    fun `데이터베이스에서 walkEntity를 가져오는 테스트`() = runTest {
        // Arrange
        val date = LocalDate.now()
        val entities = listOf(
            WalkEntity(
                id = 1,
                walkDistance = 500L,
                walkTime = 1000L,
                walkStartTime = LocalDateTime.now(),
                walkEndTime = LocalDateTime.now().plusMinutes(10),
                locationList = listOf(LatLng.from(34.753051290852135, 127.6758172363043)),
                walkCalories = 50L,
                walkDay = date
            )
        )
        coEvery { mockLocalSource.readWalkingDataWithDate(date) } returns entities

        // Act
        val result = walkingRepository.getWalkingDataWithDateFromLocal(date)

        // Assert
        assertEquals(1, result.size)
        assertEquals(500, result[0].distance)
        assertEquals(date, result[0].date)
        coVerify { mockLocalSource.readWalkingDataWithDate(date) }
    }


}