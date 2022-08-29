package com.softyorch.taskapp.data.data.userdata

import androidx.room.*
import com.softyorch.taskapp.domain.model.UserData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDataBaseDao {

    @Query("SELECT * FROM userdata_tbl")
    fun getAllUser(): Flow<List<UserData>>

    @Query("SELECT * FROM userdata_tbl WHERE id =:id")
    suspend fun getUserId(id: String): UserData

    @Query("SELECT * FROM userdata_tbl WHERE user_email=:email")
    suspend fun getUserEmail(email: String): UserData

    @Query("SELECT * FROM userdata_tbl WHERE user_email =:email AND user_pass =:password")
    suspend fun signIn(email: String, password: String): UserData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userData: UserData)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(userData: UserData)

    @Query("DELETE FROM userdata_tbl")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(userData: UserData)
}