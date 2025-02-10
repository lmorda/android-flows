package com.lmorda.androidflows

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.lmorda.androidflows.models.User
import com.lmorda.androidflows.ui.settings.SettingsScreen
import com.lmorda.androidflows.ui.settings.SettingsUiState
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSettingsScreenTitleInputLabelsAndValues() {
        composeTestRule.setContent {
            SettingsScreen(
                state = SettingsUiState(
                    user = User("John", "Doe")
                ),
                onNavigateBack = {},
                onUpdateUser = {},
            )
        }
        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("First Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("Last Name").assertIsDisplayed()
        composeTestRule.onNodeWithText("John").assertIsDisplayed()
        composeTestRule.onNodeWithText("Doe").assertIsDisplayed()
    }
}