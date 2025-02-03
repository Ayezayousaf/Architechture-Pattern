package com.architechturepattern.model.repository

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.architechturepattern.model.MediaType
import com.architechturepattern.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class VideoRepository @Inject constructor( private val context: Context) {

    private val _videoLiveData = MutableLiveData<List<Model>>()
    val videosLiveData: LiveData<List<Model>> get() = _videoLiveData
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        getVideo()
    }

     fun getVideo() {

         repositoryScope.launch {
             val video = mutableListOf<Model>()
             val uri = MediaStore.Video.Media.getContentUri("external")
             val projection = arrayOf(
                 MediaStore.Video.Media.DISPLAY_NAME,
                 MediaStore.Video.Media.SIZE,
                 MediaStore.Video.Media.DATA,
                 MediaStore.Video.Media.DURATION
             )
             val cursor = context.contentResolver.query(
                 uri,
                 projection,
                 null, null,
                 "${MediaStore.Video.Media.SIZE} ASC"
             )
             cursor?.use {
                 val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
                 val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                 val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
                 val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

                 while (cursor.moveToNext()) {
                     val name = cursor.getString(nameColumn)
                     val size = cursor.getLong(sizeColumn)
                     val path = cursor.getString(pathColumn)
                     val duration = cursor.getLong(durationColumn)

                     val minutes = duration / 1000 / 60
                     val seconds = (duration / 1000) % 60
                     val durationFormatted = String.format("%d:%02d", minutes, seconds)
                     video.add(Model(path, name, durationFormatted, size, MediaType.VIDEO))
                 }
             }
             _videoLiveData.postValue(video)
         }
     }
}