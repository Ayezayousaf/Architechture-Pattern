package com.architechturepattern.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.architechturepattern.adapters.AdapterClass
import com.architechturepattern.database.AppDatabase
import com.architechturepattern.database.FavData
import com.architechturepattern.database.FavouriteDao
import com.architechturepattern.databinding.ActivityMainBinding
import com.architechturepattern.detail_activities.DisplayAudio
import com.architechturepattern.detail_activities.DisplayImage
import com.architechturepattern.detail_activities.DisplayVideo
import com.architechturepattern.model.Model
import com.architechturepattern.model.viewmodel.AudioViewModel
import com.architechturepattern.model.viewmodel.VideoViewModel
import com.architechturepattern.model.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: ViewModel by viewModels()
    private val videoViewModel: VideoViewModel by viewModels()
    private val audioViewModel: AudioViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: AdapterClass

    var filesList: List<Model> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycleView.layoutManager = LinearLayoutManager(this)

        val i = intent.getStringExtra("MEDIA_TYPE")

        when (i) {
            "Image" -> loadImages()
            "Video" -> loadVideos()
            "Audio" -> loadAudio()
        }

        myAdapter = AdapterClass(emptyList(), object : AdapterClass.FilesListener {
            override fun onClick(pos: Int) {
                if (i == "Image") {
                    val intent = Intent(this@MainActivity, DisplayImage::class.java)
                    intent.putExtra("imageUri", filesList[pos].path)
                    startActivity(intent)
                } else if (i == "Video") {
                    val intent = Intent(this@MainActivity, DisplayVideo::class.java)
                    intent.putExtra("videoUri", filesList[pos].path)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@MainActivity, DisplayAudio::class.java)
                    intent.putExtra("audioList", ArrayList(filesList))
                    intent.putExtra("audioIndex", pos)
                    startActivity(intent)
                }
            }

            override fun onFavClick(pos: Int, isFav:Boolean) {
                CoroutineScope(Dispatchers.IO).launch {
                    val fav=AppDatabase.getDatabase(this@MainActivity).favouriteDao()
                        if(isFav){
                            fav.insert(
                                FavData(
                                    pathDB = filesList[pos].path,
                                    type = when(i){
                                        "Image"-> "IMAGES"
                                        "Video"->"VIDEOS"
                                        else->"AUDIOS"
                                    },
                                    sizeDB = filesList[pos].size,
                                    picNameDB = filesList[pos].picName,
                                    durationDB = filesList[pos].duration
                                )
                            )
                        }else{
                            fav.delete(filesList[pos].path)
                    }
                }
            }
        })
        binding.recycleView.adapter = myAdapter
    }

    private fun loadImages() {
        binding.rvTextView.text = "Images"
        viewModel.images.observe(this) { imageList ->
            filesList = imageList
            myAdapter.setData(imageList)
        }
    }

    private fun loadVideos() {

        binding.rvTextView.text = "Videos"
        videoViewModel.videos.observe(this) { videoList ->
            filesList = videoList
            myAdapter.setData(videoList)
        }
    }

    private fun loadAudio() {
        binding.rvTextView.text = "Audios"
        audioViewModel.audio.observe(this) { audioList ->
            filesList = audioList
            myAdapter.setData(audioList)
        }
    }
}
