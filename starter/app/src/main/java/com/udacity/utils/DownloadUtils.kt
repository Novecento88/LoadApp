package com.udacity.utils

import android.app.DownloadManager
import android.content.Context
import com.udacity.R

fun parseDownloadStatus(context: Context, downloadStatus: Int) : String {
    return when(downloadStatus){
        DownloadManager.STATUS_FAILED -> {
            context.getString(R.string.download_status_failed)
        }
        DownloadManager.STATUS_SUCCESSFUL -> {
            context.getString(R.string.download_status_successful)
        }
        DownloadManager.STATUS_PENDING -> {
            context.getString(R.string.download_status_pending)
        }
        DownloadManager.STATUS_RUNNING -> {
            context.getString(R.string.download_status_running)
        }
        DownloadManager.STATUS_PAUSED -> {
            context.getString(R.string.download_status_paused)
        }
        else -> {
            context.getString(R.string.download_status_error)
        }
    }
}