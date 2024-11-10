package com.example.togetherpet.Registration

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.togetherpet.R
import com.example.togetherpet.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers
import org.junit.After

@HiltAndroidTest
class RegistrationImageFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        Intents.init()
        hiltRule.inject()
        Thread.sleep(1000)
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun `testWhenClickNextButtonWithEmptyImageNavigateToNextFragment`() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )

        launchFragmentInHiltContainer<RegistrationResidenceFragment> {
            navController.setGraph(R.navigation.reg_navigation_graph)
            navController.setCurrentDestination(R.id.registrationImageFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.next_button)).perform(click())
        assert(navController.currentDestination?.id == R.id.registrationImageFragment)
    }

    @Test
    fun `testWhenClickImageChoiceButtonNavigateToImageFolder`(){
        launchFragmentInHiltContainer<RegistrationResidenceFragment>{}
        onView(withId(R.id.image_input_button)).perform(click())
        intended(IntentMatchers.hasComponent("campus.tech.kakao.map.view.SearchActivity"))
    }




}
