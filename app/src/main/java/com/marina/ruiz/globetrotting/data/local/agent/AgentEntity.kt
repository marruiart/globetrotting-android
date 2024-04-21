package com.marina.ruiz.globetrotting.data.local.agent

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marina.ruiz.globetrotting.data.repository.model.Agent

@Entity(tableName = "agent")
data class AgentEntity(
    @PrimaryKey()
    val id: String,
    val username: String,
    val nickname: String,
    val email: String,
    //val avatar: Any? = null,
    val name: String? = null,
    val surname: String? = null
) {
    fun asAgent(): Agent {
        return Agent(
            uid = id,
            username,
            nickname,
            email,
            //avatar,
            name,
            surname
        )
    }
}