package com.example.smartfarming.data.room.daos

import androidx.room.*
import com.example.smartfarming.data.room.entities.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task : Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM task_table WHERE gardenIds LIKE :gardenIds ORDER BY id DESC")
    fun getTasksForGarden(gardenIds : String) : Flow<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY id DESC")
    fun getAllTasks() : Flow<List<Task>>

}