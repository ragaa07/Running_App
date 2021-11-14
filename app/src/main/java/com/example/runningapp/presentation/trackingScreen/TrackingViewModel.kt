package com.example.runningapp.presentation.trackingScreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.domain.entites.RunsDomainModel
import com.example.runningapp.domain.usecases.RunUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackingViewModel @Inject constructor(private val useCase: RunUseCase) : ViewModel() {

    fun addRun(run: RunsDomainModel) = viewModelScope.launch(Dispatchers.IO) {
        useCase.addRun(run)
    }
}