package com.lmorda.androidflows.data

import com.lmorda.androidflows.models.VitalSigns
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FakeVitalSignsDataSource @Inject constructor() {

    fun getVitalSigns(): Flow<VitalSigns> = flow {
        var elapsedTime = 0L
        while (true) {
            if (elapsedTime >= 60000) {  // Simulate flaky heart rate device
                throw Exception("Heart rate connection unstable")
            }
            emit(
                VitalSigns(
                    bpm = Random.nextInt(85, 95),
                    systolic = Random.nextInt(110, 121),
                    diastolic = Random.nextInt(80, 91)
                )
            )
            delay(1000)
            elapsedTime += 1000
        }
    }

}