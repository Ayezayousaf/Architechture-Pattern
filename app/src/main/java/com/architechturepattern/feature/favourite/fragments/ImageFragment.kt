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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ImageFragment : Fragment() {

    private lateinit var binding: FragmentImageBinding
    private lateinit var fargAdapterRv: FragmentAdapterRv
    private lateinit var favouriteDao: FavouriteDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favouriteDao = AppDatabase.getDatabase(requireContext()).favouriteDao()

        binding.rvImageList.layoutManager = LinearLayoutManager(context)
        fargAdapterRv = FragmentAdapterRv(emptyList(), favouriteDao)

        loadFavoriteData()
        binding.rvImageList.adapter = fargAdapterRv

    }
    private fun loadFavoriteData() {
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main){
            favouriteDao.getMediaByType("IMAGES")
                .observe(viewLifecycleOwner) { favData ->
                    fargAdapterRv.setData(favData)
                }
            }
          /*  val favData = favouriteDao.getMediaByType("IMAGES")

            withContext(Dispatchers.Main) {
                fargAdapterRv.setData(favData)
            }*/
        }
    }

}
