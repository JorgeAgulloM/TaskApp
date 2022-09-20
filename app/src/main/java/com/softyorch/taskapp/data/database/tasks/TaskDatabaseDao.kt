package com.softyorch.taskapp.data.database.tasks

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDatabaseDao {

    @Query("SELECT * FROM tasks_tbl")
    fun getTasks(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks_tbl WHERE id =:id")
    suspend fun getTaskById(id: String): TaskEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskEntity: TaskEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(taskEntity: TaskEntity)

    @Query("DELETE FROM tasks_tbl")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteTask(taskEntity: TaskEntity)
}
