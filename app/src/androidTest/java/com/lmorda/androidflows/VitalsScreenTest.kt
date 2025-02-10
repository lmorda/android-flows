package com.lmorda.androidflows

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lmorda.androidflows.models.AverageBloodPressure
import com.lmorda.androidflows.models.User
import com.lmorda.androidflows.models.VitalSignsEntity
import com.lmorda.androidflows.ui.vitals.VitalSignsScreen
import com.lmorda.androidflows.ui.vitals.VitalSignsUiState
import org.junit.Rule
import org.junit.Test


class VitalSignScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testVitalsTitleAndHeartRate() {
        composeTestRule.setContent {
            VitalSignsScreen(
                state = VitalSignsUiState(
                    user = User("John", "Doe"),
                    latestVitalSigns = VitalSignsEntity(
                        id = 1,
                        timestamp = System.currentTimeMillis(),
                        bpm = 70,
                        systolic = 120,
                        diastolic = 80
                    ),
                    averageHeartRate = 70.0,
                    averageBloodPressure = AverageBloodPressure(
                        avgSystolic = 120.0,
                        avgDiastolic = 80.0
                    ),
                ),
                navigateToSettings = {},
            )
        }
        composeTestRule.onNodeWithText("John Doe").assertIsDisplayed()
        composeTestRule.onNodeWithText("Current Vitals").assertIsDisplayed()
        composeTestRule.onNodeWithText("❤\uFE0F Heart Rate: 70 bpm").assertIsDisplayed()
    }
}