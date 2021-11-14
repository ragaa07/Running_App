package com.example.runningapp.presentation.homeScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningapp.domain.usecases.RunUseCase
import com.example.runningapp.domain.util.RunsOrder
import com.example.runningapp.presentation.util.RunsEvents
import com.example.runningapp.presentation.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunsViewModel @Inject constructor(private val runsUseCase: RunUseCase) : ViewModel() {

    private val _state: MutableState<UiState> =
        mutableStateOf(UiState())
    val state: State<UiState> = _state

    init {
       getRuns(RunsOrder.DATE)
    }

    fun onEvent(event: RunsEvents) {
        when (event) {
            is RunsEvents.Order -> {
                getRuns(event.runOrder)
                _state.value = state.value.copy(
                    order = event.runOrder
                )
            }
            is RunsEvents.Delete -> {
            }
            is RunsEvents.ToggleOrderSection -> {
                _state.value = state.value.copy(
                    orderSection = !state.value.orderSection
                )

            }
        }
    }


    private fun getRuns(runsOrder: RunsOrder) {
        viewModelScope.launch(Dispatchers.IO) {
            runsUseCase.getRuns(runsOrder)
                .collect {
                    _state.value=state.value.copy(
                        runs = it
                    )
                }
        }
    }
}