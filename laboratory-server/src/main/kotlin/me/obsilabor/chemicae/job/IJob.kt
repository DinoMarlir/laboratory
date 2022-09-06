package me.obsilabor.chemicae.job

import kotlinx.coroutines.Job

interface IJob {
    val job: Job?

    fun start()

    fun stop()
}