package com.example.smartfarming.data.room.daos

import androidx.room.*
import com.example.smartfarming.data.room.entities.Garden
import kotlinx.coroutines.flow.Flow

@Dao
interface GardenDao {

    // Garden queries
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(garden: Garden)

    @Delete
    suspend fun deleteGarden(garden: Garden)

    @Update
    suspend fun updateGarden(garden: Garden)

    @Query("SELECT * FROM garden_table ORDER BY title ASC")
    fun getAllGardens() : Flow<List<Garden>>

    @Query("SELECT * FROM garden_table WHERE title = :name")
    suspend fun getGardenByName(name : String) : Garden

    @Query("SELECT * FROM garden_table WHERE id = :id")
    suspend fun getGardenById(id : Int) : Garden
}