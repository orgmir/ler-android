package app.luisramos.ler.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import app.luisramos.ler.App
import app.luisramos.ler.R
import app.luisramos.ler.ui.MainActivity

const val CHANNEL_ID = "General"

/**
 * Setup the notification channel
 *
 * Safe to always call on app start, since it will only setup once and then do nothing
 */
fun App.createNotificationChannel() {
    val name = resources.getString(R.string.notif_channel_name_general)
    val descriptionText = resources.getString(R.string.notif_channel_desc_general)
    val importance = NotificationManagerCompat.IMPORTANCE_DEFAULT
    val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
        description = descriptionText
    }
    val notificationManager = getSystemService<NotificationManager>()
    notificationManager?.createNotificationChannel(channel)
}

fun Context.showLocalNotificationForNewPosts(titles: List<String>) {
    val intent = Intent(this, MainActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    val pendingIntent =
        PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    val notif = NotificationCompat.Builder(this, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_launcher_rss_foreground)
        .setContentTitle(getString(R.string.notif_new_posts_title))
        .setContentText(getContentText(titles))
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()
    with(NotificationManagerCompat.from(this)) {
        notify(1, notif)
    }
}

private fun Context.getContentText(feedTitles: List<String>): String =
    when (feedTitles.size) {
        1 -> resources.getString(R.string.notif_new_posts_text_1, feedTitles[0])
        2 -> resources.getString(R.string.notif_new_posts_text_2, feedTitles[0], feedTitles[1])
        3 -> resources.getString(
            R.string.notif_new_posts_text_3,
            feedTitles[0],
            feedTitles[1],
            feedTitles[2]
        )
        else -> if (feedTitles.size > 3) {
            resources.getString(
                R.string.notif_new_posts_text_4,
                feedTitles[0],
                feedTitles[1]
            )
        } else {
            resources.getString(R.string.notif_new_posts_text_0)
        }
    }
