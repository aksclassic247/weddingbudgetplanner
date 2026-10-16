package com.stitchbook.app.reminders

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.stitchbook.core.ReminderMath
import java.time.ZonedDateTime

/** Schedules a daily reminder for each project that has one. */
object ReminderScheduler {

    const val CHANNEL_ID = "pattern-reminders"
    private const val FLAGS = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Knitting reminders",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply { description = "Daily reminders to work on a project" }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    fun schedule(context: Context, projectId: String, projectName: String, hour: Int, minute: Int) {
        ensureChannel(context)
        val next = ReminderMath.nextOccurrence(ZonedDateTime.now(), hour, minute)
        context.getSystemService(AlarmManager::class.java).setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            next.toInstant().toEpochMilli(),
            AlarmManager.INTERVAL_DAY,
            pendingIntent(context, projectId, projectName),
        )
    }

    fun cancel(context: Context, projectId: String) {
        val existing = PendingIntent.getBroadcast(
            context,
            requestCode(projectId),
            Intent(context, ReminderReceiver::class.java),
            FLAGS or PendingIntent.FLAG_NO_CREATE,
        ) ?: return
        context.getSystemService(AlarmManager::class.java).cancel(existing)
        existing.cancel()
    }

    private fun requestCode(projectId: String): Int = projectId.hashCode()

    private fun pendingIntent(context: Context, projectId: String, projectName: String): PendingIntent {
        val intent = Intent(context, ReminderReceiver::class.java)
            .putExtra(ReminderReceiver.EXTRA_PROJECT_ID, projectId)
            .putExtra(ReminderReceiver.EXTRA_PROJECT_NAME, projectName)
        return PendingIntent.getBroadcast(context, requestCode(projectId), intent, FLAGS)
    }
}
