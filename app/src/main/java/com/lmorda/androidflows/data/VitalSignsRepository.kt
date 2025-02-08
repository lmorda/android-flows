package com.lmorda.androidflows.data

import com.lmorda.androidflows.db.VitalSignsDao
import com.lmorda.androidflows.models.VitalSignsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VitalSignsRepository @Inject constructor(
    private val dao: VitalSignsDao,
    private val dataSource: FakeVitalSignsDataSource
) {

    // Use a hot flow to broadcast errors immediately to multiple consumers
    private val _errorMessageFlow = MutableSharedFlow<String>(replay = 0)
    val errorMessageFlow: SharedFlow<String> = _errorMessageFlow

    // Cold flows to fetch vital signs data from the database, only fetch when needed (subscribed)
    fun getAllVitalSigns() = dao.getAllVitalSigns()
    fun getLatestVitalSigns() = dao.getLatestVitalSigns()
    fun getAvgHeartRateToday() = dao.getAverageHeartRateForToday()
    fun getAvgBloodPressureToday() = dao.getAverageBloodPressureForToday()

    // Dispatch onto the IO thread
    fun getVitals() {
        CoroutineScope(Dispatchers.IO).launch {
            // Cold flow to fetch heart rate and blood pressure data from the fake data source, only fetched when needed
            dataSource.getVitalSigns()
                // Catch errors, emit on the error hot flow, and retry
                .retryWhen { cause, _ ->
                    _errorMessageFlow.emit("${cause.localizedMessage}, trying to reconnect")
                    delay(3000)
                    true // Always retry
                }
                .collect { vitals ->
                    dao.insert(
                        VitalSignsEntity(
                            timestamp = System.currentTimeMillis(),
                            bpm = vitals.bpm,
                            systolic = vitals.systolic,
                            diastolic = vitals.diastolic
                        )
                    )
                }
        }
    }

}