package com.example.runningapp.domain.entites

import android.graphics.Bitmap

data class RunsDomainModel(
    var id: Int? = null,
    var img: Bitmap? = null,
    var timestamp: Long = 0L,
    var avgSpeedInKMH: Float = 0F,
    var distanceInMeters: Int = 0,
    var timeInMillis: Long = 0L,
    var burnedCalories: Int = 0
)
