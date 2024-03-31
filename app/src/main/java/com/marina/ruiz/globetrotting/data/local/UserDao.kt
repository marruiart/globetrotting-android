package com.marina.ruiz.globetrotting.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(userEntity: UserEntity)

    @Query("SELECT * FROM user WHERE id = 1")
    fun getUser(): Flow<UserEntity>

    @Query("DELETE FROM user WHERE id = 1")
    suspend fun deleteUser()
}