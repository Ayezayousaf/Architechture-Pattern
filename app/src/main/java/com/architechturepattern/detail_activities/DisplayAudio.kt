package com.architechturepattern.detail_activities

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Looper
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.architechturepattern.R
import com.architechturepattern.databinding.ActivityDisplayAudioBinding
import com.architechturepattern.model.Model
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit


class DisplayAudio : AppCompatActivity() {
    private lateinit var binding: ActivityDisplayAudioBinding
    private var mediaPlayer: MediaPlayer? = null
    private var handler = android.os.Handler(Looper.getMainLooper())
    private var audioList: ArrayList<Model>? = null
    private var currentAudioIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.audioList = intent.getSerializableExtra("audioList") as? ArrayList<Model>

        currentAudioIndex = intent.getIntExtra("audioIndex", 0)
/*
        if (audioList.isNullOrEmpty()) {
            Toast.makeText(this, "No audio files found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }*/

        playAudio(currentAudioIndex)

        // Play/Pause button
        binding.btnPlay.setOnClickListener {
            mediaPlayer?.let {
                if (it.isPlaying) {
                    it.pause()
                    binding.btnPlay.setImageResource(R.drawable.play)
                } else {
                    it.start()
                    binding.btnPlay.setImageResource(R.drawable.pause)
                    updateSeekBar()
                }
            }
        }

        // Next button
        binding.btnNext.setOnClickListener {
            if (currentAudioIndex < audioList!!.size - 1) {
                currentAudioIndex++
                playAudio(currentAudioIndex)
            }
        }

        // Previous button
        binding.btnPrev.setOnClickListener {
            if (currentAudioIndex > 0) {
                currentAudioIndex--
                playAudio(currentAudioIndex)
            }
        }

        // SeekBar change listener
        binding.audioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun playAudio(index: Int) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            try {
                val audio = audioList!![index]
                setDataSource(audio.path)
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                prepareAsync()
                setOnPreparedListener {
                    binding.btnPlay.setImageResource(R.drawable.pause)
                    start()
                    binding.audioSeekBar.max = duration
                    updateSeekBar()
                    setAudioDetails(audio)
                }
                setOnCompletionListener {
                    binding.btnPlay.setImageResource(R.drawable.play)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@DisplayAudio, "Error playing audio", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setAudioDetails(audio: Model) {
        // Load audio thumbnail
        val thumbnail = getAudioThumbnail(audio.path)
        Glide.with(this)
            .load(thumbnail)
            .placeholder(R.drawable.music)
            .into(binding.viewAudioImage)

        // Set audio name and duration
        binding.audioText.text = audio.picName
        binding.endDuration.text = formatDuration(mediaPlayer?.duration ?: 0)
        binding.startDuration.text = formatDuration(0)
    }

    private fun updateSeekBar() {
        mediaPlayer?.let {
            binding.audioSeekBar.progress = it.currentPosition
            binding.startDuration.text = formatDuration(it.currentPosition)
            handler.postDelayed({ updateSeekBar() }, 1000)
        }
    }

    private fun formatDuration(duration: Int): String {
        return String.format(
            "%02d:%02d",
            TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
            TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) % 60
        )
    }

    private fun getAudioThumbnail(filePath: String): ByteArray? {
        return try {
            val retriever = android.media.MediaMetadataRetriever()
            retriever.setDataSource(filePath)
            val thumbnail = retriever.embeddedPicture
            retriever.release()
            thumbnail
        } catch (e: Exception) {
            null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
