package com.example.workmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.workmanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val downLoadImageRequest: WorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>().build()

        binding.downloadButton.setOnClickListener {
            binding.progressBar.isVisible = true
            WorkManager.getInstance(this).enqueue(downLoadImageRequest)
        }

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(downLoadImageRequest.id)
            .observe(this) { workInfo ->

                workInfo?.outputData?.getString("uri")?.let {
                    binding.progressBar.isVisible = false
                    binding.imageView.setImageURI(it.toUri())
                }
            }
    }
}