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

class ImageRepository @Inject constructor(private val context: Context) {

    private val _imagesLiveData = MutableLiveData<List<Model>>()
    val imagesLiveData: LiveData<List<Model>> get() = _imagesLiveData

    private val repositoryScope = CoroutineScope(Dispatchers.IO)

    init {
        getImages()
    }

    fun getImages() {
        repositoryScope.launch {
            val images = mutableListOf<Model>()
            val uri = MediaStore.Images.Media.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATA
            )
            context.contentResolver.query(
                uri,
                projection,
                null, null,
                "${MediaStore.Images.Media.SIZE} ASC"
            )
                ?.use { cursor ->
                    val pathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    val nameColumn =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                    val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                    Log.d("TAG", "getImages")
                    while (cursor.moveToNext()) {

                        val name = cursor.getString(nameColumn)
                        val size = cursor.getLong(sizeColumn)
                        val path = cursor.getString(pathColumn)

                        images.add(Model(path, name, "", size, MediaType.IMAGE))
                    }
                    _imagesLiveData.postValue(images)
                }
        }

    }
}

