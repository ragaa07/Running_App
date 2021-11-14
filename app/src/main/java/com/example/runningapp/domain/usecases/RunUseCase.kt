package com.example.runningapp.domain.usecases

import com.example.runningapp.domain.dataInterfaces.RunsRepository
import com.example.runningapp.domain.entites.RunsDomainModel
import com.example.runningapp.domain.util.RunsOrder
import kotlinx.coroutines.flow.Flow

class RunUseCase(private val runsRepository: RunsRepository) {


    suspend fun addRun(run: RunsDomainModel) {
        runsRepository.addRun(run)
    }

    suspend fun deleteRun(run: RunsDomainModel) {
        runsRepository.deleteRun(run)
    }

     fun getAllRunsOrderByDate(): Flow<List<RunsDomainModel>> {
        return runsRepository.getAllRunsOrderByDate()
    }

    private fun getAllRunsOrderByAvgSpeed(): Flow<List<RunsDomainModel>> {
        return runsRepository.getAllRunsOrderByAvgSpeed()
    }

    private fun getAllRunsOrderByDistance(): Flow<List<RunsDomainModel>> {
        return runsRepository.getAllRunsOrderByDistance()
    }

    private fun getAllRunsOrderByRunningTime(): Flow<List<RunsDomainModel>> {
        return runsRepository.getAllRunsOrderByRunningTime()
    }

    private fun getAllRunsOrderByBurnedCalories(): Flow<List<RunsDomainModel>> {
        return runsRepository.getAllRunsOrderByBurnedCalories()
    }

    fun getRuns(runsOrder: RunsOrder ): Flow<List<RunsDomainModel>> {
        return when (runsOrder) {
            RunsOrder.TIME -> {
                getAllRunsOrderByRunningTime()
            }
            RunsOrder.DATE -> {
                getAllRunsOrderByDate()
            }
            RunsOrder.SPEED -> {
                getAllRunsOrderByAvgSpeed()
            }
            RunsOrder.CALORIES -> {
                getAllRunsOrderByBurnedCalories()
            }
            RunsOrder.DISTANCE -> {
                getAllRunsOrderByDistance()
            }
        }
    }

    fun getTotalDistance(): Flow<Int> {
        return runsRepository.getTotalDistance()
    }

    fun getTotalTimeInMillis(): Flow<Long> {
        return runsRepository.getTotalTimeInMillis()
    }

    fun getTotalBurnedCalories(): Flow<Int> {
        return runsRepository.getTotalBurnedCalories()
    }

    fun getTotalAvgSpeed(): Flow<Float> {
        return runsRepository.getTotalAvgSpeed()
    }
}