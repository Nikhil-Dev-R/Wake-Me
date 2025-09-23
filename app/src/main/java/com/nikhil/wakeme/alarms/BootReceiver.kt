package com.nikhil.wakeme.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nikhil.wakeme.data.AlarmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        if (action == Intent.ACTION_BOOT_COMPLETED || action == Intent.ACTION_MY_PACKAGE_REPLACED) {
            val appContext = context.applicationContext ?: return

            CoroutineScope(Dispatchers.IO).launch {
                val repo = AlarmRepository(appContext)
                val alarms = repo.getEnabledAlarmsList()
                alarms.forEach { alarm ->
                    AlarmScheduler.scheduleAlarm(appContext, alarm)
                }
            }
        }
    }
}
