package com.jnu.togetherpet.reportViewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jnu.togetherpet.data.entity.MissingEntity
import com.jnu.togetherpet.data.entity.ReportEntity
import com.jnu.togetherpet.data.repository.MissingRepository
import com.jnu.togetherpet.data.repository.ReportRepository
import com.jnu.togetherpet.ui.fragment.searching.enums.ButtonType
import com.jnu.togetherpet.ui.viewmodel.report.ReportDataViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

/*초기값 확인*/
@OptIn(ExperimentalCoroutinesApi::class)
class ReportDataViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ReportDataViewModel
    private val mockReportRepository: ReportRepository = mockk(relaxed = true)
    private val mockMissingRepository: MissingRepository = mockk(relaxed = true)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ReportDataViewModel(mockReportRepository, mockMissingRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selectedBtn 초기값 확인`() = runTest {
        assertEquals(ButtonType.MISSING, viewModel.selectedButton.value)
    }

    @Test
    fun `MISSING 버튼 타입 업데이트 확인`() = runTest {
        viewModel.updateSelectedBtn(ButtonType.MISSING)
        assertEquals(ButtonType.MISSING, viewModel.selectedButton.value)
    }

    @Test
    fun `REPORT 버튼 타입 업데이트 확인`() = runTest {
        viewModel.updateSelectedBtn(ButtonType.REPORT)
        assertEquals(ButtonType.REPORT, viewModel.selectedButton.value)
    }

    @Test
    fun `MuPet 버튼 타입 업데이트 확인`() = runTest {
        viewModel.updateSelectedBtn(ButtonType.MyPET)
        assertEquals(ButtonType.MyPET, viewModel.selectedButton.value)
    }

    @Test
    fun `StateFlow 초기값 확인`() = runTest {
        assertTrue(viewModel.missingReports.first().isEmpty())
        assertTrue(viewModel.myPetReports.first().isEmpty())
        assertTrue(viewModel.nearbySuspectedReports.first().isEmpty())

        assertNull(viewModel.missingDetail.first())
        assertNull(viewModel.suspectedDetail.first())
    }
}