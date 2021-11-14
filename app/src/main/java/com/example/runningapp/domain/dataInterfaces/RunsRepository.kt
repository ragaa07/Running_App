package com.example.runningapp.domain.dataInterfaces

import com.example.runningapp.domain.entites.RunsDomainModel
import kotlinx.coroutines.flow.Flow

interface RunsRepository {

    suspend fun addRun(run: RunsDomainModel)
    suspend fun deleteRun(run: RunsDomainModel)
    fun getAllRunsOrderByDate(): Flow<List<RunsDomainModel>>
    fun getAllRunsOrderByAvgSpeed(): Flow<List<RunsDomainModel>>
    fun getAllRunsOrderByDistance(): Flow<List<RunsDomainModel>>
    fun getAllRunsOrderByRunningTime(): Flow<List<RunsDomainModel>>
    fun getAllRunsOrderByBurnedCalories(): Flow<List<RunsDomainModel>>
    fun getTotalDistance():Flow<Int>
    fun getTotalTimeInMillis():Flow<Long>
    fun getTotalBurnedCalories():Flow<Int>
    fun getTotalAvgSpeed():Flow<Float>
}