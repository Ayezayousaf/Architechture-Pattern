package com.architechturepattern.model

import java.io.Serializable


data class Model(
    val path: String="",
    val picName: String="",
    val duration:String ="",
    val size:Long =0,
    val type: MediaType,
    var isFavourite:Boolean=false
):Serializable

enum class MediaType {
    IMAGE, VIDEO, AUDIO
}
