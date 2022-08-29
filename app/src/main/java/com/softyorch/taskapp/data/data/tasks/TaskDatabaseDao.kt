package com.softyorch.taskapp.data.data.tasks

import androidx.room.*
import com.softyorch.taskapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDatabaseDao {

    @Query("SELECT * FROM tasks_tbl")
    fun getTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks_tbl WHERE id =:id")
    suspend fun getTaskById(id: String): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(task: Task)

    @Query("DELETE FROM tasks_tbl")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteTask(task: Task)
}
