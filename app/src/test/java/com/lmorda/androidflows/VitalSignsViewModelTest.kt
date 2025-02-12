import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.lmorda.androidflows.data.UserDataRepository
import com.lmorda.androidflows.data.VitalSignsRepository
import com.lmorda.androidflows.models.AverageBloodPressure
import com.lmorda.androidflows.models.User
import com.lmorda.androidflows.models.VitalSignsEntity
import com.lmorda.androidflows.ui.vitals.VitalSignsUiState
import com.lmorda.androidflows.ui.vitals.VitalSignsViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
class VitalSignsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private val userDataRepository: UserDataRepository = mockk()
    private val vitalSignsRepository: VitalSignsRepository = mockk()
    private lateinit var viewModel: VitalSignsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN all data sources WHEN fetched THEN returns correct state`() = runTest {
        val user = User("John", "Doe")
        val vitalSigns = VitalSignsEntity(1, 1627847284000, 72, 120, 80)
        val avgHeartRate = 75.0
        val avgBloodPressure = AverageBloodPressure(120.0, 80.0)
        val history = listOf(
            VitalSignsEntity(1, 1627847284000, 70, 118, 78),
            VitalSignsEntity(2, 1627847384000, 68, 115, 75)
        )

        coEvery { userDataRepository.getUser() } returns user
        coEvery { vitalSignsRepository.getLatestVitalSigns() } returns flowOf(vitalSigns)
        coEvery { vitalSignsRepository.getAvgHeartRateToday() } returns flowOf(avgHeartRate)
        coEvery { vitalSignsRepository.getAvgBloodPressureToday() } returns flowOf(avgBloodPressure)
        coEvery { vitalSignsRepository.getAllVitalSigns() } returns flowOf(history)

        viewModel = VitalSignsViewModel(vitalSignsRepository, userDataRepository)

        viewModel.state.test {
            assertEquals(VitalSignsUiState(user = null), awaitItem())
            assertEquals(
                VitalSignsUiState(
                    user = User(firstName = "John", lastName = "Doe"),
                    latestVitalSigns = null,
                    averageHeartRate = null,
                    averageBloodPressure = null,
                    vitalSignsHistory = emptyList()
                ),
                awaitItem()
            )
            assertEquals(
                VitalSignsUiState(
                    user = User(firstName = "John", lastName = "Doe"),
                    latestVitalSigns = VitalSignsEntity(
                        id = 1,
                        timestamp = 1627847284000,
                        bpm = 72,
                        systolic = 120,
                        diastolic = 80
                    ),
                    averageHeartRate = 75.0,
                    averageBloodPressure = AverageBloodPressure(
                        avgSystolic = 120.0,
                        avgDiastolic = 80.0
                    ),
                    vitalSignsHistory = listOf(
                        VitalSignsEntity(
                            id = 1,
                            timestamp = 1627847284000,
                            bpm = 70,
                            systolic = 118,
                            diastolic = 78
                        ),
                        VitalSignsEntity(
                            id = 2,
                            timestamp = 1627847384000,
                            bpm = 68,
                            systolic = 115,
                            diastolic = 75
                        )
                    )
                ),
                awaitItem()
            )
        }
    }
}
