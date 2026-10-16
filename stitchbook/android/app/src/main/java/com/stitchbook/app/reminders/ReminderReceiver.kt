package com.stitchbook.app.reminders

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.stitchbook.app.R
import com.stitchbook.app.data.ContentRepository
import com.stitchbook.app.data.ProjectStore

/** Shows the reminder notification, and re-arms reminders after the phone restarts. */
class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            rescheduleAll(context)
        } else {
            showNotification(context, intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val projectId = intent.getStringExtra(EXTRA_PROJECT_ID).orEmpty()
        val name = intent.getStringExtra(EXTRA_PROJECT_NAME).orEmpty()
        val notification = NotificationCompat.Builder(context, ReminderScheduler.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Time to knit")
            .setContentText(if (name.isBlank()) "Pick up your project." else "Pick up $name.")
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(projectId.hashCode(), notification)
    }

    private fun rescheduleAll(context: Context) {
        val content = ContentRepository.load(context)
        ProjectStore(context).load().forEach { project ->
            val hour = project.reminderHour
            val minute = project.reminderMinute
            if (hour != null && minute != null) {
                val title = content.pattern(project.patternId)?.title.orEmpty()
                ReminderScheduler.schedule(context, project.id, title, hour, minute)
            }
        }
    }

    companion object {
        const val EXTRA_PROJECT_ID = "projectId"
        const val EXTRA_PROJECT_NAME = "projectName"
    }
}
