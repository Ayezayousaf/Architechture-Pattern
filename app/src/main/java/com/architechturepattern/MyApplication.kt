package com.architechturepattern

import android.app.Application
import android.content.Context
import com.architechturepattern.model.repository.AudioRepository
import com.architechturepattern.model.repository.ImageRepository
import com.architechturepattern.model.repository.VideoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
@HiltAndroidApp
class MyApplication :Application(){
        @Module
        @InstallIn(SingletonComponent::class)
        object AppModule {

            @Provides
            fun provideRepository(@ApplicationContext context: Context): ImageRepository {
                return ImageRepository(context)
            }
            @Provides
            fun provideVideoRepository(@ApplicationContext context: Context): VideoRepository {
                return VideoRepository(context)
            }
            @Provides
            fun provideAudioRepository(@ApplicationContext context: Context): AudioRepository {
                return AudioRepository(context)
            }
        }
}
