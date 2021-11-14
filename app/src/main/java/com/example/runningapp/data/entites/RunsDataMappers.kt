package com.example.runningapp.data.entites

import com.example.runningapp.domain.entites.RunsDomainModel

fun RunsDataModel.toDomainModel(): RunsDomainModel {
    return RunsDomainModel(
        id, img, timestamp, avgSpeedInKMH, distanceInMeters, timeInMillis, burnedCalories
    )
}

fun RunsDomainModel.toDataModel(): RunsDataModel {
    return RunsDataModel(
        id, img, timestamp, avgSpeedInKMH, distanceInMeters, timeInMillis, burnedCalories
    )
}

fun List<RunsDataModel>.toDomainList(): List<RunsDomainModel> {
    val runsDomainList: MutableList<RunsDomainModel> = mutableListOf()
    for (run in this) {
        runsDomainList.add(
            RunsDomainModel(
                id = run.id,
                img = run.img,
                timestamp = run.timestamp,
                avgSpeedInKMH = run.avgSpeedInKMH,
                distanceInMeters = run.distanceInMeters,
                timeInMillis = run.timeInMillis,
                burnedCalories = run.burnedCalories
            )
        )
    }
    return runsDomainList
}
