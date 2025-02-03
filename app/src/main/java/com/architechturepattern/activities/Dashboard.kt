package com.architechturepattern.activities

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import androidx.appcompat.app.AppCompatActivity
import com.architechturepattern.databinding.ActivityDashboardBinding
import com.architechturepattern.feature.favourite.Favourites


class Dashboard : AppCompatActivity(){

    private lateinit var binding: ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener() {
            intentCall("Image")
        }
        binding.button2.setOnClickListener() {
            intentCall("Video")
        }
        binding.button3.setOnClickListener() {
            intentCall("Audio")
        }
        binding.button4.setOnClickListener(){
            val intent=Intent(this, Favourites::class.java)
            startActivity(intent)

        }
        val totalStorage = getTotalStorage()
        val freeStorage = getAvailableStorage()
        val progressCount=((freeStorage.toDouble() / totalStorage)*100).toFloat()
        binding.totalSpaceBlock.text=formatSize(totalStorage)
        binding.usedSpaceBlock.text=formatSize(freeStorage)
        binding.circularProgressBar.progress=progressCount
        binding.progressText.text="${progressCount.toInt()}%"
    }

    fun getTotalStorage(): Long {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return totalBlocks * blockSize
    }
    fun getAvailableStorage(): Long {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return availableBlocks * blockSize
    }
    fun formatSize(size: Long): String {
        val kb = size / 1024
        val mb = kb / 1024
        val gb = mb / 1024
        return when {
            gb > 0 -> "$gb GB"
            mb > 0 -> "$mb MB"
            kb > 0 -> "$kb KB"
            else -> "$size Bytes"
        }
    }
    private fun intentCall(mediaType: String){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("MEDIA_TYPE", mediaType)
        startActivity(intent)
    }
}