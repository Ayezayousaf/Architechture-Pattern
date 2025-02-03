package com.architechturepattern.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.architechturepattern.R
import com.architechturepattern.activities.ExtraFunctions
import com.architechturepattern.database.FavData
import com.architechturepattern.database.FavouriteDao
import com.architechturepattern.databinding.FavItemListBinding
import com.bumptech.glide.Glide

class FragmentAdapterRv(private var dataList: List<FavData>, private val favouriteDao: FavouriteDao) :
    RecyclerView.Adapter<FragmentAdapterRv.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = FavItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(dataList[position])
        }

        override fun getItemCount(): Int = dataList.size

        fun setData(newData: List<FavData>) {
            this.dataList = newData
            notifyDataSetChanged()
        }

        inner class ViewHolder(private val binding: FavItemListBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun bind(favData: FavData) {
                val formattedSize = ExtraFunctions.formatFileSize(favData.sizeDB)

                when(favData.type){
                    "IMAGES"->{
                        Glide.with(binding.root.context)
                            .load(favData.pathDB)
                            .placeholder(R.drawable.picdefault)
                            .into(binding.fragImageView)
                    }
                    "VIDEOS"->{
                        Glide.with(binding.root.context)
                            .load(favData.pathDB)
                            .placeholder(R.drawable.picdefault)
                            .into(binding.fragImageView)
                        binding.playImage.visibility = View.VISIBLE
                        binding.duration.text = "Duration: ${favData.durationDB}"
                    }
                    "AUDIOS"->{
                        val thumbnail = ExtraFunctions.getAudioThumbnail(favData.pathDB)
                        Glide.with(binding.root.context)
                            .load(thumbnail).placeholder(R.drawable.music)
                            .into(binding.fragImageView)
                        binding.duration.text = "Duration: ${favData.durationDB}"
                    }
                }
                binding.fName.text = favData.picNameDB
                binding.fSizes.text = "Size: $formattedSize"
            }
        }
    }

