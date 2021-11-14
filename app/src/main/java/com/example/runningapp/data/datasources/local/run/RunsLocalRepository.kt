package com.example.runningapp.data.datasources.local.run

import com.example.runningapp.data.entites.RunsDataModel

class RunsLocalRepository(private val runsDao: RunsDao) {

    suspend fun addRun(run: RunsDataModel) {
        runsDao.addRun(run = run)
    }

    suspend fun deleteRun(run: RunsDataModel) {
        runsDao.deleteRun(run = run)
    }

    fun getAllRunsOrderByDate(): List<RunsDataModel> {
        try {
            return runsDao.getAllRunsOrderByDate()
        }catch (e:Exception) {
            return emptyList()
        }

    }

    fun getAllRunsOrderByAvgSpeed(): List<RunsDataModel> {
        return runsDao.getAllRunsOrderByAvgSpeed()
    }

    fun getAllRunsOrderByDistance(): List<RunsDataModel> {
        return runsDao.getAllRunsOrderByDistance()
    }

    fun getAllRunsOrderByRunningTime(): List<RunsDataModel> {
        return runsDao.getAllRunsOrderByRunningTime()
    }

    fun getAllRunsOrderByBurnedCalories(): List<RunsDataModel> {
        return runsDao.getAllRunsOrderByBurnedCalories()
    }

    fun getTotalDistance(): Int {
        return runsDao.getTotalDistance()
    }

    fun getTotalTimeInMillis(): Long {
        return runsDao.getTotalTimesInMillis()
    }

    fun getTotalBurnedCalories(): Int {
        return runsDao.getTotalCaloriesBurned()
    }
    fun getTotalAvgSpeed(): Float {
        return runsDao.getTotalAvgSpeed()
    }
}