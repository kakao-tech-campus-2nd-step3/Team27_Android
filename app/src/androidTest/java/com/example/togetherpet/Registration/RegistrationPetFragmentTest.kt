package com.example.togetherpet.Registration

import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
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
class RegistrationPetFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        hiltRule.inject()
        Thread.sleep(1000)
    }

    @Test
    fun `testWhenClickNextButtonWithEmptyInputFieldNavigateToNextFragment`(){
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer<RegistrationPetFragment>{
            navController.setGraph(R.navigation.reg_navigation_graph)
            navController.setCurrentDestination(R.id.registrationPetFragment)
            Navigation.setViewNavController(requireView(), navController)
        }
        onView(withId(R.id.next_button)).perform(click())
        assert(navController.currentDestination?.id == R.id.registrationPetFragment)
    }

    @Test
    fun `testWhenClickNextButtonNavigateToNextFragment`(){
        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer<RegistrationPetFragment>{
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
}