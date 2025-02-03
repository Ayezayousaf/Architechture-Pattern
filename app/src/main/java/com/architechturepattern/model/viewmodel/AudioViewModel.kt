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
class AudioViewModel @Inject constructor(
    private val audioRepository: AudioRepository
) : ViewModel() {
    val audio: LiveData<List<Model>> get() = audioRepository.audiosLiveData

}