package com.jnu.togetherpet.Registration

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jnu.togetherpet.R
import com.jnu.togetherpet.launchFragmentInHiltContainer
import com.jnu.togetherpet.ui.fragment.registration.RegistrationPetFragment
import com.jnu.togetherpet.ui.viewmodel.report.RegistrationViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RegistrationPetFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
        Thread.sleep(1000)
    }

    @Test
    fun `testWhenClickNextButtonWithEmptyInputFieldNavigateToNextFragment`() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        launchFragmentInHiltContainer<RegistrationPetFragment> {
            navController.setGraph(R.navigation.reg_navigation_graph)
            navController.setCurrentDestination(R.id.registrationPetFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.next_button)).perform(click())
        assert(navController.currentDestination?.id == R.id.registrationPetFragment)
    }

    @Test
    fun `testWhenClickNextButtonNavigateToNextFragment`() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        launchFragmentInHiltContainer<RegistrationPetFragment> {
            navController.setGraph(R.navigation.reg_navigation_graph)
            navController.setCurrentDestination(R.id.registrationPetFragment)
            Navigation.setViewNavController(requireView(), navController)

        }

        onView(withId(R.id.name_input_field)).perform(replaceText("꾸릉이"))
        onView(withId(R.id.age_input_field)).perform(replaceText("3"))
        onView(withId(R.id.species_input_field)).perform(replaceText("말티즈"))
        onView(withId(R.id.button_neutering_true)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.next_button)).perform(click())

        assert(navController.currentDestination?.id == R.id.registrationResidenceFragment)
    }

    @Test
    fun `testWhenClickNextButtonNavigateToNextFragmentWithSave`() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        var collectedPetName: String? = null
        var collectedPetAge: Long? = null
        var collectedSpecies: String? = null
        var collectedNeutering: Boolean? = null

        var job : Job? = null
        var jobTwo : Job? = null
        var jobThree : Job? = null
        var jobFour : Job? = null

        launchFragmentInHiltContainer<RegistrationPetFragment> {
            navController.setGraph(R.navigation.reg_navigation_graph)
            navController.setCurrentDestination(R.id.registrationPetFragment)
            Navigation.setViewNavController(requireView(), navController)
            val viewModel: RegistrationViewModel by activityViewModels()

                job = lifecycleScope.launch {
                    viewModel.petName.collect { value ->
                        collectedPetName = value
                    }
                }
                jobTwo = lifecycleScope.launch {
                    viewModel.petAge.collect { value ->
                        collectedPetAge = value
                    }
                }
                jobThree = lifecycleScope.launch {
                    viewModel.petSpecies.collect { value ->
                        collectedSpecies = value
                    }
                }
                jobFour = lifecycleScope.launch {
                    viewModel.neutering.collect { value ->
                        collectedNeutering = value
                    }
            }
        }

        onView(withId(R.id.name_input_field)).perform(replaceText("꾸릉이"))
        onView(withId(R.id.age_input_field)).perform(replaceText("3"))
        onView(withId(R.id.species_input_field)).perform(replaceText("말티즈"))
        onView(withId(R.id.button_neutering_true)).perform(click())
        Thread.sleep(1000L)
        onView(withId(R.id.next_button)).perform(click())

        Thread.sleep(1000L)

        // StateFlow의 업데이트된 값 확인
        assert("꾸릉이" == collectedPetName)
        assert(3L == collectedPetAge)
        assert("말티즈" == collectedSpecies)
        assert(true == collectedNeutering)

        job?.cancel() // 테스트 종료 시 수집을 중단합니다.
        jobTwo?.cancel()
        jobThree?.cancel()
        jobFour?.cancel()

        assert(navController.currentDestination?.id == R.id.registrationResidenceFragment)
    }
}
