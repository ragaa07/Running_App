package com.example.runningapp.data.repositories

import com.example.runningapp.data.datasources.local.run.RunsLocalRepository
import com.example.runningapp.data.entites.toDataModel
import com.example.runningapp.data.entites.toDomainList
import com.example.runningapp.domain.dataInterfaces.RunsRepository
import com.example.runningapp.domain.entites.RunsDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RunsRepositoryImpl(
    private val runsLocalRepository: RunsLocalRepository
) : RunsRepository {
    override suspend fun addRun(run: RunsDomainModel) {
        runsLocalRepository.addRun(run.toDataModel())
    }

    override suspend fun deleteRun(run: RunsDomainModel) {
        runsLocalRepository.deleteRun(run.toDataModel())
    }

    override fun getAllRunsOrderByDate(): Flow<List<RunsDomainModel>> = flow {
        emit(runsLocalRepository.getAllRunsOrderByDate().toDomainList())
    }

    override fun getAllRunsOrderByAvgSpeed(): Flow<List<RunsDomainModel>> = flow {
        emit(runsLocalRepository.getAllRunsOrderByAvgSpeed().toDomainList())
    }

    override fun getAllRunsOrderByDistance(): Flow<List<RunsDomainModel>> = flow {
        emit(runsLocalRepository.getAllRunsOrderByDistance().toDomainList())
    }

    override fun getAllRunsOrderByRunningTime(): Flow<List<RunsDomainModel>> = flow {
        emit(runsLocalRepository.getAllRunsOrderByRunningTime().toDomainList())
    }

    override fun getAllRunsOrderByBurnedCalories(): Flow<List<RunsDomainModel>> = flow {
        emit(runsLocalRepository.getAllRunsOrderByBurnedCalories().toDomainList())
    }

    override fun getTotalDistance(): Flow<Int> = flow {
        emit(runsLocalRepository.getTotalDistance())
    }

    override fun getTotalTimeInMillis(): Flow<Long> = flow {
        emit(runsLocalRepository.getTotalTimeInMillis())
    }

    override fun getTotalBurnedCalories(): Flow<Int> = flow {
        emit(runsLocalRepository.getTotalBurnedCalories())
    }

    override fun getTotalAvgSpeed(): Flow<Float> = flow {
        emit(runsLocalRepository.getTotalAvgSpeed())
    }
}