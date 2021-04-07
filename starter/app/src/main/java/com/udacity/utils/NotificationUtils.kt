package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.udacity.Constants.DOWNLOAD_STATUS
import com.udacity.Constants.FILE_NAME
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0
private val FLAGS = 0

fun NotificationManager.sendNotification(applicationContext: Context, messageBody: String, fileName: String, downloadStatus: String) {

    val mainContentIntent = Intent(applicationContext, MainActivity::class.java)
    mainContentIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

    val mainPendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        mainContentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val detailContentIntent = Intent(applicationContext, DetailActivity::class.java)
    detailContentIntent.putExtra(FILE_NAME, fileName)
    detailContentIntent.putExtra(DOWNLOAD_STATUS, downloadStatus)

    val detailPendingIntent = PendingIntent.getActivity(
            applicationContext,
            REQUEST_CODE,
            detailContentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

    val detailAction = NotificationCompat.Action(null, applicationContext.getString(R.string.notification_detail_action), detailPendingIntent)

    val image = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_assistant_black_24dp
    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(mainPendingIntent)
        .setAutoCancel(true)
        .setLargeIcon(image)
            .addAction(detailAction)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}