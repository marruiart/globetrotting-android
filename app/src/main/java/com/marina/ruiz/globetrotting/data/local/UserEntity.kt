package com.marina.ruiz.globetrotting.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.marina.ruiz.globetrotting.data.repository.model.User

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey()
    val id: Int = 1,
    val uid: String,
    val username: String,
    val nickname: String,
    val email: String,
    //val avatar: Any? = null,
    val name: String? = null,
    val surname: String? = null,
    val age: String? = null
) {
    fun asUser(): User {
        return User(
            uid,
            username,
            nickname,
            email,
            name,
            surname,
            age
        )
    }
}