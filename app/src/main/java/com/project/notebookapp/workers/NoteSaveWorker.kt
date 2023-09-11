package com.project.notebookapp.workers

import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.project.notebookapp.R
import com.project.notebookapp.repo.NoteRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
class NoteSaveWorker(context: Context, workerParams: WorkerParameters,
    ) : Worker(context, workerParams), KoinComponent {

    private val noteRepository: NoteRepository by inject()
    override fun doWork(): Result {
        return try {
            val numberOfNotes = noteRepository.getNumberOfNotes()
            showSuccessNotification(numberOfNotes)
            Result.success()

        } catch (e: Exception) {
            showErrorNotification()
            Result.retry()
        }
    }

    private fun showSuccessNotification(numberOfNotes: Int) {

        val notificationBuilder = NotificationCompat.Builder(applicationContext, NOTE_SAVE_CHANNEL_ID)
            .setContentTitle("Note Update")
            .setContentText("You have $numberOfNotes notes.")
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        Log.d("MYtAG", "SUCCESSFUL!!!")
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID_SUCCESS, notificationBuilder)
    }

    private fun showErrorNotification() {
        val notification = NotificationCompat.Builder(applicationContext, NOTE_SAVE_CHANNEL_ID)
            .setContentTitle("Error Saving Notes")
            .setContentText("There was an error while saving notes.")
            .setSmallIcon(R.drawable.ic_notification)
            .build()
        Log.d("MYtAG", "FAILED!!!")

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID_ERROR, notification)
    }

    companion object {
        const val NOTE_SAVE_CHANNEL_ID = "note_channel"
        private const val NOTIFICATION_ID_SUCCESS = 1
        private const val NOTIFICATION_ID_ERROR = 2
    }
}