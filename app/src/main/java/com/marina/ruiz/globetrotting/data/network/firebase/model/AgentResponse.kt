package com.marina.ruiz.globetrotting.data.network.firebase.model

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.local.agent.AgentEntity
import com.marina.ruiz.globetrotting.data.repository.model.Agent
import kotlinx.parcelize.Parcelize

@Parcelize
data class AgentResponse(
    val uid: String,
    val username: String,
    val nickname: String,
    val email: String,
    //val avatar: Any? = null, TODO include type
    val name: String? = null,
    val surname: String? = null,
    //val bookings: List<BookingResponse> = emptyList(), // TODO move to BookingResponse
) : Parcelable {

    fun asAgentEntity(): AgentEntity {
        return AgentEntity(
            id = uid,
            username = username,
            nickname = nickname,
            email = email,
            //avatar = avatar,
            name = name,
            surname = surname
        )
    }
}

fun List<AgentResponse>.asAgentsEntityList(): List<AgentEntity> {
    return this.map { it.asAgentEntity() }
}
