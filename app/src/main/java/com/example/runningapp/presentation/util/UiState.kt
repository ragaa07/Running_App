package com.example.runningapp.presentation.util

import com.example.runningapp.domain.entites.RunsDomainModel
import com.example.runningapp.domain.util.RunsOrder

data class UiState(
    val runs:List<RunsDomainModel> = listOf(),
    val orderSection:Boolean = false,
    val order:RunsOrder= RunsOrder.DATE
)