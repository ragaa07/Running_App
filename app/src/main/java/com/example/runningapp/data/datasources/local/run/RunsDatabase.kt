package com.example.runningapp.data.datasources.local.run

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.runningapp.data.util.Converters
import com.example.runningapp.data.entites.RunsDataModel


@Database(entities = [RunsDataModel::class], version = 1)

@TypeConverters(Converters::class)
abstract class RunsDatabase : RoomDatabase() {

    abstract val runsDao: RunsDao
}