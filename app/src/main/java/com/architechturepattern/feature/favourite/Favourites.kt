package com.architechturepattern.feature.favourite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.architechturepattern.R
import com.architechturepattern.databinding.ActivityFavouritesBinding
import com.architechturepattern.feature.favourite.fragments.AudioFragment
import com.architechturepattern.adapters.FragmentAdapter
import com.architechturepattern.feature.favourite.fragments.ImageFragment
import com.architechturepattern.feature.favourite.fragments.VideoFragment
import com.google.android.material.tabs.TabLayoutMediator

class Favourites : AppCompatActivity() {
    private lateinit var binding: ActivityFavouritesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = listOf(ImageFragment(), VideoFragment(), AudioFragment())
        val adapter = FragmentAdapter(fragment, supportFragmentManager, lifecycle)
        binding.viewPager2.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Images"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.image_)
                }

                1 -> {
                    tab.text = "Videos"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.video)
                }

                else -> {
                    tab.text = "Audios"
                    tab.icon = ContextCompat.getDrawable(this, R.drawable.musicfile)
                }
            }
        }.attach()
    }
}
