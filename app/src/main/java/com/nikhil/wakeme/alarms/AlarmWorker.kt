package com.nikhil.wakeme.alarms

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class AlarmWorker(
    context: Context, params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val alarmId = inputData.getLong(AlarmScheduler.EXTRA_ALARM_ID, -1L)
        val type = inputData.getString(AlarmScheduler.EXTRA_TYPE) ?: "MAIN"
        if (alarmId == -1L) return Result.failure()

        return try {
            when (type) {
                "UPCOMING" -> AlarmHandler.handleUpcoming(applicationContext, alarmId)
                else -> AlarmHandler.handleTrigger(applicationContext, alarmId)
            }
            Result.success()
        } catch (t: Throwable) {
            Result.retry()
        }
    }
}
