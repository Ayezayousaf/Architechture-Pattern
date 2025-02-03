package com.architechturepattern.activities

import android.media.MediaMetadataRetriever

class ExtraFunctions {
 companion object{
     fun getAudioThumbnail(filePath: String): ByteArray? {
         return try {
             val retriever = MediaMetadataRetriever()
             retriever.setDataSource(filePath)
             val thumbnail = retriever.embeddedPicture
             retriever.release()
             thumbnail
         } catch (e: Exception) {
             null
         }
     }
     fun formatFileSize(sizeInBytes: Long): String {
         return when {
             sizeInBytes < 1024 -> {
                 "$sizeInBytes Bytes"
             }

             sizeInBytes < 1024 * 1024 -> {
                 String.format("%.2f KB", sizeInBytes / 1024.0)
             }

             sizeInBytes < 1024 * 1024 * 1024 -> {
                 String.format("%.2f MB", sizeInBytes / (1024.0 * 1024))
             }

             else -> {
                 String.format("%.2f GB", sizeInBytes / (1024.0 * 1024 * 1024))
             }
         }
     }
 }


}