package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DelayProjectDao {
    @Query("SELECT * FROM delay_projects ORDER BY dateCreated DESC")
    fun getAllProjects(): Flow<List<DelayProjectEntity>>

    @Query("SELECT * FROM delay_projects WHERE id = :id")
    suspend fun getProjectById(id: Long): DelayProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: DelayProjectEntity): Long

    @Update
    suspend fun updateProject(project: DelayProjectEntity)

    @Delete
    suspend fun deleteProject(project: DelayProjectEntity)

    @Query("DELETE FROM delay_projects WHERE id = :id")
    suspend fun deleteById(id: Long)
}
