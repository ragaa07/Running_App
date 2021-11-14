package com.example.runningapp.di

import android.app.Application
import androidx.room.Room
import com.example.runningapp.data.datasources.local.run.RunsDatabase
import com.example.runningapp.data.datasources.local.run.RunsLocalRepository
import com.example.runningapp.data.repositories.RunsRepositoryImpl
import com.example.runningapp.domain.dataInterfaces.RunsRepository
import com.example.runningapp.domain.usecases.RunUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRunDatabase(app: Application): RunsDatabase {
        return Room.databaseBuilder(
            app,
            RunsDatabase::class.java,
            "running_dp"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRunsLocalDatabase(dp: RunsDatabase): RunsLocalRepository {
        return RunsLocalRepository(dp.runsDao)
    }

    @Provides
    @Singleton
    fun provideRunsRepository(runsLocalRepository: RunsLocalRepository): RunsRepository {
        return RunsRepositoryImpl(runsLocalRepository)
    }

    @Provides
    @Singleton
    fun provideRunsUseCase(runsRepository: RunsRepository):RunUseCase{
        return RunUseCase(runsRepository)
    }
}