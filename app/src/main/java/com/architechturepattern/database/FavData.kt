package com.architechturepattern.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favourite")
data class FavData(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val pathDB: String="",
    val picNameDB: String="",
    val durationDB:String ="",
    val sizeDB:Long =0,
    val type:String=""
)
