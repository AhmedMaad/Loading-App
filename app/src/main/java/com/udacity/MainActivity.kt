package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.udacity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        //Note: No need for "isClickable = true", because setOnClickListener makes the View clickable
        binding.mainContent.customButton.setOnClickListener {
            val checkedRBID = binding.mainContent.group.checkedRadioButtonId
            if (checkedRBID == -1)
                Toast.makeText(this, R.string.select_file, Toast.LENGTH_SHORT).show();
            else {
                val selectedFile = when (checkedRBID) {
                    R.id.load_app_rb -> LOAD_APP_URL
                    R.id.glide_rb -> GLIDE_URL
                    else -> RETROFIT_URL
                }
                download(selectedFile)
            }

        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(selectedFile: String) {
        val request =
            DownloadManager.Request(selectedFile.toUri())
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val LOAD_APP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
        private const val GLIDE_URL = "https://github.com/bumptech/glide"
        private const val RETROFIT_URL = "https://github.com/square/retrofit"

        private const val CHANNEL_ID = "channelId"
    }
}