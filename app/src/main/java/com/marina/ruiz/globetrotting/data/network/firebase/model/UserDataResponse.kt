package com.marina.ruiz.globetrotting.data.network.firebase.model

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.local.user.UserEntity
import com.marina.ruiz.globetrotting.data.repository.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDataResponse(
    val uid: String,
    val username: String,
    val nickname: String,
    val email: String,
    //val avatar: Any? = null, TODO include type
    val name: String? = null,
    val surname: String? = null,
    val age: String? = null,
    //val bookings: List<BookingResponse> = emptyList(), // TODO move to BookingResponse
    //val favorites?: ClientFavDestination[] // TODO receive client favs
) : Parcelable {

    fun asUserEntity(): UserEntity {
        return UserEntity(
            uid = uid,
            username = username,
            nickname = nickname,
            email = email,
            //avatar = avatar,
            name = name,
            surname = surname,
            age = age
        )
    }

    fun asUser(): User {
        return User(
            uid,
            username,
            nickname,
            email,
            //avatar,
            name,
            surname,
            age
        )
    }
}

fun Map<String, Any>.asUserDataResponse(): UserDataResponse {
    return UserDataResponse(
        this["user_id"] as String,
        this["username"] as String,
        this["nickname"] as String,
        this["email"] as String,
        //this["avatar"],
        this["name"] as String?,
        this["surname"] as String?,
        this["age"] as String?
    )
}
