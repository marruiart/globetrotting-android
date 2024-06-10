package com.marina.ruiz.globetrotting.data.network.firebase.model.response

import android.os.Parcelable
import com.marina.ruiz.globetrotting.data.local.favorite.FavoriteEntity
import com.marina.ruiz.globetrotting.data.local.user.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDataResponse(
    val uid: String,
    val username: String,
    val nickname: String,
    val email: String,
    val avatar: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val age: String? = null,
    val favorites: ArrayList<HashMap<String, String>>?
) : Parcelable {

    fun asUserEntity(): UserEntity {
        return UserEntity(
            uid = uid,
            username = username,
            nickname = nickname,
            email = email,
            avatar = avatar,
            name = name,
            surname = surname,
            age = age
        )
    }

    fun asFavoritesEntity(): List<FavoriteEntity> {
        return favorites?.map { fav ->
            FavoriteEntity(
                id = fav["fav_id"] as String,
                destinationId = fav["destination_id"] as String
            )
        } ?: emptyList()
    }
}

fun Map<String, Any>.asUserDataResponse(): UserDataResponse {
    return UserDataResponse(
        this["user_id"] as String,
        this["username"] as String,
        this["nickname"] as String,
        this["email"] as String,
        this["avatar"] as? String,
        this["name"] as String?,
        this["surname"] as String?,
        this["age"] as String?,
        this["favorites"] as ArrayList<HashMap<String, String>>?
    )
}
