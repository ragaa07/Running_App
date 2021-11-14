package com.example.runningapp.presentation.util

import com.example.runningapp.domain.entites.RunsDomainModel
import com.example.runningapp.domain.util.RunsOrder

sealed class RunsEvents {
    data class Order(val runOrder: RunsOrder) : RunsEvents()
    data class Delete(val run: RunsDomainModel) : RunsEvents()
    object ToggleOrderSection : RunsEvents()
}