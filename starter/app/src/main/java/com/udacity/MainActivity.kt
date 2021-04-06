package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.Constants.GLIDE_URL
import com.udacity.Constants.RETROFIT_URL
import com.udacity.Constants.UDACITY_PROJECT_URL
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var url : String = ""
    private var notificationMessageBody = ""

    private lateinit var notificationManager: NotificationManager
    private lateinit var downloadManager: DownloadManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_name)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        radio_group.setOnCheckedChangeListener { group, checkedId ->
            when(radio_group.checkedRadioButtonId){
                R.id.radio_glide -> {
                    url = GLIDE_URL
                    notificationMessageBody = getString(R.string.notification_body_glide)
                }

                R.id.radio_udacity -> {
                    url = UDACITY_PROJECT_URL
                    notificationMessageBody = getString(R.string.notification_body_udacity)
                }

                R.id.radio_retrofit -> {
                    url = RETROFIT_URL
                    notificationMessageBody = getString(R.string.notification_body_retrofit)
                }

                else -> {
                    return@setOnCheckedChangeListener
                }
            }
        }

        custom_button.setOnClickListener {
            if(radio_group.checkedRadioButtonId == -1){
                return@setOnClickListener
            }
            download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE && downloadID == id) {
                custom_button.setState(ButtonState.Completed)

                val query = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))

                if (context != null && query.moveToFirst()) {
                    notificationManager.sendNotification(context, notificationMessageBody)
                }
            }
        }
    }

    private fun createChannel(channelId: String, channelName: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)
            }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            notificationChannel.enableVibration(true)
            notificationChannel.description = applicationContext.getString(R.string.notification_description)

            notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
        custom_button.setState(ButtonState.Loading)
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
