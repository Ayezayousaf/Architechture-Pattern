package com.architechturepattern.model.repository

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.architechturepattern.model.MediaType
import com.architechturepattern.model.Model
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AudioRepository @Inject constructor(private val context: Context) {

    private val _audioLiveData = MutableLiveData<List<Model>>()
    val audiosLiveData: LiveData<List<Model>> get() = _audioLiveData
    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        getAudio()
    }

    @SuppressLint("DefaultLocale")
    fun getAudio() {
        repositoryScope.launch {
            val audio = mutableListOf<Model>()
            val uri = MediaStore.Audio.Media.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA,
//                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,
            )
            context.contentResolver.query(
                uri,
                projection,
                null, null,
                "${MediaStore.Audio.Media.DISPLAY_NAME} ASC"
            )
                ?.use { cursor ->
                    val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                    val nameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                    val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                    val durationColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)

                    while (cursor.moveToNext()) {
                        val name = cursor.getString(nameColumn)
                        val size = cursor.getLong(sizeColumn)
                        val path = cursor.getString(pathColumn)
                        val duration = cursor.getLong(durationColumn)

                        val minutes = duration / 1000 / 60
                        val seconds = (duration / 1000) % 60
                        val durationFormatted = String.format("%d:%02d", minutes, seconds)

                        audio.add(Model(path, name, durationFormatted, size, MediaType.AUDIO, true))
                    }
                }
            _audioLiveData.postValue(audio)
        }
    }
}