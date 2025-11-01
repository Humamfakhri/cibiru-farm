package org.khiot.iotcibiruwetan.data.model

import com.google.firebase.Timestamp

data class KebunCabeData(
    val valveOpen1: Boolean = false,
    val valveOpen2: Boolean = false,
    val mode: String = "manual",
    val soil1: Soil = Soil(),
    val soil2: Soil = Soil(),
    val timestamp: String = Timestamp.now().toString()
)

data class Soil(
    val status: String = "Kering",
    val value: Long = 0L
)

data class FormattedTime(
    val text: String,
    val isStale: Boolean
)