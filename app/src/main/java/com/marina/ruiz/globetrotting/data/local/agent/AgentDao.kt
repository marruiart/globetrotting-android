package com.marina.ruiz.globetrotting.data.local.agent

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AgentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createAgents(agentsListEntity: List<AgentEntity>)

    @Query("SELECT * FROM agent")
    fun getAllAgents(): Flow<List<AgentEntity>>

    @Query("DELETE FROM agent")
    suspend fun deleteAgents()
}