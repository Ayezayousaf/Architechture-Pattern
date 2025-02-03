package com.architechturepattern.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.architechturepattern.R
import com.architechturepattern.activities.ExtraFunctions
import com.architechturepattern.database.FavouriteDao
import com.architechturepattern.databinding.ItemListBinding
import com.architechturepattern.model.MediaType
import com.architechturepattern.model.Model
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdapterClass(private var mediaList: List<Model>, var listener: FilesListener) :
    RecyclerView.Adapter<AdapterClass.ViewHolder>() {

    private lateinit var favouriteDao: FavouriteDao

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mediaList[position])
    }

    inner class ViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(data: Model) {
            val formattedSize = ExtraFunctions.formatFileSize(data.size)
            when (data.type) {
                MediaType.IMAGE -> {
                    Glide.with(binding.root.context)
                        .load(data.path)
                        .into(binding.imageView)
                }

                MediaType.VIDEO -> {
                    Glide.with(binding.root.context)
                        .load(data.path)
                        .placeholder(R.drawable.picdefault)
                        .into(binding.imageView)
                    binding.playImage.visibility = View.VISIBLE
                    binding.duration.text = "Duration: ${data.duration}"
                }

                MediaType.AUDIO -> {
                    val thumbnail = ExtraFunctions.getAudioThumbnail(data.path)
                    Glide.with(binding.root.context)
                        .load(thumbnail).placeholder(R.drawable.music)
                        .into(binding.imageView)
                    binding.duration.text = "Duration: ${data.duration}"
                }
            }
            binding.size.text = "Size: $formattedSize"
            binding.name.text = (data.picName)

            itemView.setOnClickListener() {
                listener.onClick(position)
            }
                CoroutineScope(Dispatchers.IO).launch {
                    if (favouriteDao.isPathExists(data.path)) {
                        binding.imageButton.isSelected = data.isFavourite
                        binding.imageButton.setImageResource(
                            if (data.isFavourite) R.drawable.favourite else R.drawable.unfavourite
                        )

                    }
                }
            binding.imageButton.setOnClickListener() {
                binding.imageButton.setImageResource(
                    if (data.isFavourite) R.drawable.favourite else R.drawable.unfavourite)
                listener.onFavClick(adapterPosition, data.isFavourite)
            }
        }
    }
    fun setData(mediaList: List<Model>) {
        this.mediaList = mediaList
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return mediaList.size
    }

    interface FilesListener {
        fun onClick(pos: Int)
        fun onFavClick(pos: Int,isFav:Boolean)
    }

}

