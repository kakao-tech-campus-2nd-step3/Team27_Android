package com.jnu.togetherpet.Registration

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.*
import com.jnu.togetherpet.R
import com.jnu.togetherpet.launchFragmentInHiltContainer
import com.jnu.togetherpet.ui.fragment.registration.RegistrationResidenceFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RegistratinoResidenceFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
        Thread.sleep(1000)
    }

    @Test
    fun `testWhenClickNextButtonNavigateToNextFragment`() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        launchFragmentInHiltContainer<RegistrationResidenceFragment> {
            navController.setGraph(R.navigation.reg_navigation_graph)
            navController.setCurrentDestination(R.id.registrationResidenceFragment)
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.residence_input_field)).perform(replaceText("북구 용봉동"))
        onView(withId(R.id.feature_input_field)).perform(replaceText("작음"))
        Thread.sleep(1000)

        onView(withId(R.id.next_button)).perform(click())

        assert(navController.currentDestination?.id == R.id.registrationImageFragment)
    }
}
