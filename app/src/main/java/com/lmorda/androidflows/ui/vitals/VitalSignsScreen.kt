package com.lmorda.androidflows.ui.vitals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lmorda.androidflows.R
import com.lmorda.androidflows.models.AverageBloodPressure
import com.lmorda.androidflows.models.VitalSignsEntity
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries

@Composable
internal fun VitalSignsScreen(
    state: VitalSignsUiState,
    navigateToSettings: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${state.user?.firstName} ${state.user?.lastName}",
                style = MaterialTheme.typography.headlineSmall,
            )
            Icon(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(20.dp)
                    .clickable { navigateToSettings.invoke() },
                imageVector = Icons.Filled.Edit,
                contentDescription = "Settings",
            )
        }
        Spacer(modifier = Modifier.height(36.dp))
        CurrentVitals(latestVitalSigns = state.latestVitalSigns)
        Spacer(modifier = Modifier.height(48.dp))
        DailyAverage(state.averageHeartRate, state.averageBloodPressure)
        Spacer(modifier = Modifier.height(48.dp))
        HeartRate(state.vitalSignsHistory)
    }
}

@Composable
private fun CurrentVitals(latestVitalSigns: VitalSignsEntity?) {
    Text(
        text = stringResource(R.string.current_vitals),
        style = MaterialTheme.typography.headlineSmall,
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(
            R.string.heart_rate_bpm,
            latestVitalSigns?.bpm ?: "--"
        ),
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        text = stringResource(
            R.string.blood_pressure_mmhg, latestVitalSigns?.systolic
                ?: "--", latestVitalSigns?.diastolic ?: "--"
        ),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun DailyAverage(
    averageHeartRate: Double?,
    averageBloodPressure: AverageBloodPressure?
) {
    Text(
        text = stringResource(R.string.daily_average),
        style = MaterialTheme.typography.headlineSmall,
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = stringResource(
            R.string.avg_hr_today_bpm,
            averageHeartRate?.toInt() ?: "--"
        ),
        style = MaterialTheme.typography.titleLarge
    )
    Text(
        text = stringResource(
            R.string.avg_bp_today_mmhg,
            averageBloodPressure?.avgSystolic?.toInt() ?: "--",
            averageBloodPressure?.avgDiastolic?.toInt() ?: "--"
        ),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun HeartRate(vitalSignsHistory: List<VitalSignsEntity>) {
    Text(
        text = stringResource(R.string.heart_rate_chart),
        style = MaterialTheme.typography.headlineSmall,
    )
    Spacer(modifier = Modifier.height(16.dp))
    HeartRateChart(vitalSignsHistory)
}

@Composable
private fun HeartRateChart(vitalSignsHistory: List<VitalSignsEntity>) {
    if (vitalSignsHistory.isEmpty()) return
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(vitalSignsHistory) {
        modelProducer.runTransaction {
            val heartRateValues = vitalSignsHistory.map { it.bpm }.takeLast(50)
            lineSeries { series(heartRateValues) }
        }
    }
    CartesianChartHost(
        chart = rememberCartesianChart(
            rememberLineCartesianLayer(
                pointSpacing = 4.dp,
                rangeProvider = CartesianLayerRangeProvider.fixed(minY = 70.0, maxY = 110.0)
            ),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(label = null),
        ),
        modelProducer = modelProducer,
    )
}