package com.architechturepattern.detail_activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.architechturepattern.databinding.ActivityDisplayImageBinding
import com.bumptech.glide.Glide

class DisplayImage : AppCompatActivity() {
    private lateinit var binding:ActivityDisplayImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityDisplayImageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val imageUri = intent.getStringExtra("imageUri")
        Glide.with(this).load(imageUri).into(binding.imageDisplay)
    }
}