package com.jnu.togetherpet.Registration

import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jnu.togetherpet.R
import com.jnu.togetherpet.launchFragmentInHiltContainer
import com.jnu.togetherpet.ui.fragment.registration.RegistrationImageFragment
import com.jnu.togetherpet.ui.viewmodel.report.RegistrationViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


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

        launchFragmentInHiltContainer<RegistrationImageFragment> {
            navController.setGraph(R.navigation.reg_navigation_graph)
            navController.setCurrentDestination(R.id.registrationImageFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.next_button)).perform(click())
        assert(navController.currentDestination?.id == R.id.registrationImageFragment)
    }

    @Test
    fun `testWhenClickImageChoiceButtonNavigateToImageFolder`() {
        launchFragmentInHiltContainer<RegistrationImageFragment> {}
        Thread.sleep(1000)
        onView(withId(R.id.image_input_button)).perform(click())
        intended(hasAction(Intent.ACTION_GET_CONTENT))
    }

    @Test
    fun `testWhenClickNextButtonWithImageNavigateToNextFragment`() {
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )


        launchFragmentInHiltContainer<RegistrationImageFragment> {
            navController.setGraph(R.navigation.reg_navigation_graph)
            navController.setCurrentDestination(R.id.registrationImageFragment)
            Navigation.setViewNavController(requireView(), navController)
            requireView().findViewById<ImageView>(R.id.animal_image)
                .setImageResource(R.drawable.emergency_icon)
            val viewModel: RegistrationViewModel by activityViewModels()

            viewModel.setPetImage(Uri.parse("android.resource://${context?.packageName}/${R.drawable.emergency_icon}"))
        }
        Thread.sleep(1000)
        onView(withId(R.id.animal_image)).check(matches(isDisplayed()))
        onView(withId(R.id.next_button)).perform(click())
        assert(navController.currentDestination?.id == R.id.registrationNicknameFragment)
    }

}

