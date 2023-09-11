package com.project.notebookapp.ui

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.project.notebookapp.di.appModule
import com.project.notebookapp.workers.NoteSaveWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import java.util.concurrent.TimeUnit

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BaseApplication)
            modules(appModule)
        }

        createNotificationChannel()

        scheduleNoteSave()
    }

    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    private fun scheduleNoteSave(){
        val saveNoteWorkRequest = PeriodicWorkRequest.Builder(
            NoteSaveWorker::class.java,
            10,
            TimeUnit.SECONDS
        ).build()

        WorkManager.getInstance(this).enqueue(saveNoteWorkRequest)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "note_channel"
            val channelName = "Note Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.cancelAll()

        WorkManager.getInstance(this).cancelAllWork()
    }
}

