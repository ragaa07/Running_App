package com.example.runningapp.data.entites

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "running_table")
data class RunsDataModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var img: Bitmap? = null,
    var timestamp: Long = 0L,
    var avgSpeedInKMH: Float = 0F,
    var distanceInMeters: Int = 0,
    var timeInMillis: Long = 0L,
    var burnedCalories: Int = 0
)
