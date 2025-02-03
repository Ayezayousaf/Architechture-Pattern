package com.architechturepattern.feature.favourite.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.architechturepattern.R
import com.architechturepattern.adapters.FragmentAdapterRv
import com.architechturepattern.database.AppDatabase
import com.architechturepattern.database.FavouriteDao
import com.architechturepattern.databinding.FragmentImageBinding
import com.architechturepattern.databinding.FragmentVideoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoFragment : Fragment() {


    private lateinit var binding: FragmentVideoBinding
    private lateinit var fargAdapterRv: FragmentAdapterRv
    private lateinit var favouriteDao: FavouriteDao

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentVideoBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            favouriteDao = AppDatabase.getDatabase(requireContext()).favouriteDao()

            binding.videoList.layoutManager = LinearLayoutManager(context)
            fargAdapterRv = FragmentAdapterRv(emptyList(), favouriteDao)

            loadFavoriteData()
            binding.videoList.adapter = fargAdapterRv

        }
        private fun loadFavoriteData() {
            lifecycleScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main){
                    favouriteDao.getMediaByType("VIDEOS")
                        .observe(viewLifecycleOwner) { favData ->
                            fargAdapterRv.setData(favData)
                        }
                }
            /*    val favData = favouriteDao.getMediaByType("VIDEOS")

                withContext(Dispatchers.Main) {
                    fargAdapterRv.setData(favData)
                }*/
            }
        }

}