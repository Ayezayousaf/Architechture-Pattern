package com.architechturepattern.model.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.architechturepattern.model.Model
import com.architechturepattern.model.repository.AudioRepository
import com.architechturepattern.model.repository.ImageRepository
import com.architechturepattern.model.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class VideoViewModel @Inject constructor(
    private val videoRepository: VideoRepository
) : ViewModel() {
    val videos: LiveData<List<Model>> get() = videoRepository.videosLiveData

}