package com.lmorda.androidflows

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.lmorda.androidflows.data.UserDataRepository
import com.lmorda.androidflows.models.User
import com.lmorda.androidflows.ui.settings.SettingsUiState
import com.lmorda.androidflows.ui.settings.SettingsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val repository: UserDataRepository = mockk()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN fake user data source success WHEN fetch user data THEN returns user name`() = runTest {
        coEvery { repository.getUser() } returns User("John",  "Doe")
        viewModel = SettingsViewModel(repository)
        viewModel.state.test {
            assertEquals(SettingsUiState(User("", "")), awaitItem())
            assertEquals(SettingsUiState(User("John", "Doe")), awaitItem())
        }

    }
}