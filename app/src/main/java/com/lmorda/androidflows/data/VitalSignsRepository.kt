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

    private val _errorFlow = MutableSharedFlow<String>(replay = 0)
    val errorFlow: SharedFlow<String> = _errorFlow

    fun getAllVitalSigns() = dao.getAllVitalSigns()
    fun getLatestVitalSigns() = dao.getLatestVitalSigns()
    fun getAvgHeartRateToday() = dao.getAverageHeartRateForToday()
    fun getAvgBloodPressureToday() = dao.getAverageBloodPressureForToday()

    fun getVitals() {
        CoroutineScope(Dispatchers.IO).launch {
            dataSource.getVitalSigns()
                .retryWhen { cause, _ ->
                    _errorFlow.emit("${cause.localizedMessage}, trying to reconnect")
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