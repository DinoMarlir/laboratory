package me.obsilabor.laboratory.arch

import kotlinx.serialization.Serializable

@Serializable
data class ScheduledTask(
    val action: SchedulableAction,
    val hour: Int,
    val minute: Int
)