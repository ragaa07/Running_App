package com.example.runningapp.data.datasources.local.run

import androidx.room.*
import com.example.runningapp.data.entites.RunsDataModel

@Dao
interface RunsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addRun(run: RunsDataModel)

    @Delete
    suspend fun deleteRun(run: RunsDataModel)

    @Query("SELECT* FROM running_table ORDER BY timestamp")
    fun getAllRunsOrderByDate(): List<RunsDataModel>

    @Query("SELECT* FROM running_table ORDER BY timestamp")
    fun getAllRunsOrderByRunningTime(): List<RunsDataModel>

    @Query("SELECT* FROM running_table ORDER BY avgSpeedInKMH")
    fun getAllRunsOrderByAvgSpeed(): List<RunsDataModel>

    @Query("SELECT* FROM running_table ORDER BY distanceInMeters")
    fun getAllRunsOrderByDistance(): List<RunsDataModel>

    @Query("SELECT* FROM running_table ORDER BY burnedCalories")
    fun getAllRunsOrderByBurnedCalories(): List<RunsDataModel>

    @Query("SELECT SUM(timeInMillis) FROM running_table")
    fun getTotalTimesInMillis(): Long

    @Query("SELECT SUM(burnedCalories) FROM running_table")
    fun getTotalCaloriesBurned(): Int

    @Query("SELECT SUM(distanceInMeters) FROM running_table")
    fun getTotalDistance(): Int

    @Query("SELECT AVG(avgSpeedInKMH) FROM running_table")
    fun getTotalAvgSpeed(): Float

}