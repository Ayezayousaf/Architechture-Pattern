package com.architechturepattern.feature.favourite.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.architechturepattern.database.AppDatabase
import com.architechturepattern.databinding.FragmentImageBinding
import com.architechturepattern.database.FavouriteDao
import com.architechturepattern.adapters.FragmentAdapterRv
import androidx.lifecycle.lifecycleScope
import com.architechturepattern.databinding.FragmentAudioBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioFragment : Fragment() {

    private lateinit var binding: FragmentAudioBinding
    private lateinit var fargAdapterRv: FragmentAdapterRv
    private lateinit var favouriteDao: FavouriteDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouriteDao = AppDatabase.getDatabase(requireContext()).favouriteDao()

        binding.audioList.layoutManager = LinearLayoutManager(context)
        fargAdapterRv = FragmentAdapterRv(emptyList(), favouriteDao)

        loadFavoriteData()
        binding.audioList.adapter = fargAdapterRv

    }

    private fun loadFavoriteData() {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                favouriteDao.getMediaByType("AUDIOS")
                    .observe(viewLifecycleOwner) { favData ->
                        fargAdapterRv.setData(favData)
                    }
            }
        }
    }
}
