package com.example.workmanager

import android.content.Context
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DownloadWorker(
    private val context: Context,
    private val params: WorkerParameters
) : CoroutineWorker(
    appContext = context,
    params = params
) {
    override suspend fun doWork(): Result {
        delay(2000)
        val response = DownloadApi.instance.downloadImage()

        return if (response.isSuccessful) {
            val responseBody = response.body()
            return withContext(Dispatchers.IO) {
                val file = File(context.cacheDir, "image.jpg")
                val outputStream = FileOutputStream(file)
                outputStream.use { stream ->
                    try {
                        stream.write(responseBody?.bytes())
                    } catch(e: IOException) {
                        return@withContext Result.failure(
                            workDataOf(
                                "problem" to e.localizedMessage
                            )
                        )
                    }
                }
                Result.success(
                    workDataOf(
                        "uri" to file.toUri().toString()
                    )
                )
            }
        } else {
            Result.failure()
        }
    }
}