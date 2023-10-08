package hr.vlahov.newsdemo.sign_up_sign_in

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import hr.vlahov.domain.usecases.ProfileUseCase
import hr.vlahov.newsdemo.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SignUpSignInTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var profileUseCase: ProfileUseCase

    @Before
    fun setupAppNavHost() {
        hiltRule.inject()
    }

    @Test
    fun test_start_destination() {
        composeTestRule
            .onNodeWithContentDescription("Splash Screen")
            .assertIsDisplayed()
    }

    @Test
    fun test_sign_up_flow() {
        runTest {
            delay(5000)
            val allProfiles = profileUseCase.allProfiles.stateIn(this)
            if (profileUseCase.isProfileSelected())
                composeTestRule
                    .onNodeWithContentDescription("News main")
                    .assertIsDisplayed()
            else if (profileUseCase.isProfileSelected().not() && allProfiles.value.isNotEmpty())
                composeTestRule
                    .onNodeWithContentDescription("Choose profile")
                    .assertIsDisplayed()
            else
                composeTestRule
                    .onNodeWithContentDescription("Create profile")
                    .assertIsDisplayed()
        }
    }
}