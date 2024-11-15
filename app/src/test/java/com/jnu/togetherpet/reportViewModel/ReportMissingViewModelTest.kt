package com.jnu.togetherpet.reportViewModel

import com.jnu.togetherpet.data.repository.ReportRepository
import com.jnu.togetherpet.ui.fragment.searching.enums.ReportStatus
import com.jnu.togetherpet.ui.viewmodel.report.ReportMissingViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class ReportMissingViewModelTest {
    private lateinit var viewModel: ReportMissingViewModel
    private val mockReportRepository: ReportRepository = mockk(relaxed = true)  //반환 X
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ReportMissingViewModel(mockReportRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `데이터 전송 성공 시 ReportStatus가 SUCCESS로 변경`() = runTest {
        val color = "흰색"
        val gender = "남"
        val breed = "말티즈"
        val description = "낯 안가림"
        val foundDate = "2024년 11월 15일 14:30"
        val latitude = 37.7749
        val longitude = -122.4194
        val missingId = 1L
        val fileList = listOf<File>()

        // When
        viewModel.reportMissingObserve(
            color, gender, breed, description, foundDate, latitude, longitude, missingId, fileList
        )
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify {
            mockReportRepository.registerReportByMissing(
                color, latitude, longitude, "2024-11-15 14:30:00", description, breed, gender, missingId, fileList
            )
        }
        assertEquals(ReportStatus.IDLE, viewModel.reportStatus.first())
    }
}