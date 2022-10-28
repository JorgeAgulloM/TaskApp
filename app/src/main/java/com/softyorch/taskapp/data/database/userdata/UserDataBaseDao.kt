package com.softyorch.taskapp.data.database.userdata

import androidx.room.*

@Dao
interface UserDataBaseDao {

    @Query("SELECT * FROM userdata_tbl")
    suspend fun getData(): UserDataEntity?

    @Query("SELECT * FROM userdata_tbl WHERE id =:id")
    suspend fun getUserId(id: String): UserDataEntity?

    @Query("SELECT * FROM userdata_tbl WHERE user_email=:email")
    suspend fun getUserEmail(email: String): UserDataEntity?

    @Query("SELECT * FROM userdata_tbl WHERE user_email =:email AND user_pass =:password")
    suspend fun signIn(email: String, password: String): UserDataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userDataEntity: UserDataEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(userDataEntity: UserDataEntity)

    @Query("DELETE FROM userdata_tbl")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(userDataEntity: UserDataEntity)
}