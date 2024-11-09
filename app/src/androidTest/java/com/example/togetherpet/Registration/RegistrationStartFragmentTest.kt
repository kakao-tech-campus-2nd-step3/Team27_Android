package com.example.togetherpet.Registration

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.togetherpet.R
import com.example.togetherpet.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class RegistrationStartFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `testWhenClickNextButtonMoveToNextFragment`(){
        val navController : NavController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer<RegistrationStartFragment>{
            navController.setGraph(R.navigation.reg_navigation_graph)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.start_white_button)).perform(click())

        assert(navController.currentDestination?.id == R.id.registrationPetFragment)
    }
}