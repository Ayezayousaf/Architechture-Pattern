package com.architechturepattern.detail_activities

import android.os.Bundle
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import com.architechturepattern.databinding.ActivityDisplayVideoBinding

class DisplayVideo : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayVideoBinding
    private lateinit var mediaController: MediaController
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityDisplayVideoBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val videoUri = intent.getStringExtra("videoUri")
        mediaController=MediaController(this)
        binding.videoDisplay.setVideoPath(videoUri)
        binding.videoDisplay.setMediaController(mediaController)
        mediaController.setAnchorView(binding.videoDisplay)
        binding.videoDisplay.start()
    }
}