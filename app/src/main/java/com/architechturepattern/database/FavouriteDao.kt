package com.architechturepattern.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.architechturepattern.database.FavData

@Dao
interface FavouriteDao {
    @Insert
    suspend fun insert(favData: FavData)

    @Update
    suspend fun update(favData: FavData)

    @Query("DELETE FROM favourite WHERE pathDB=:filePath")
    suspend fun delete(filePath: String)

    @Query("SELECT EXISTS(SELECT 1 FROM Favourite WHERE pathDB = :filePath)")
    suspend fun isPathExists(filePath: String): Boolean

    @Query("SELECT * FROM Favourite")
    suspend fun getFavData(): List<FavData>

    @Query("SELECT * FROM Favourite WHERE type = :type")
     fun getMediaByType(type: String): LiveData<List<FavData>>
}