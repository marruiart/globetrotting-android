package com.marina.ruiz.globetrotting.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.marina.ruiz.globetrotting.data.local.agent.AgentDao
import com.marina.ruiz.globetrotting.data.local.agent.AgentEntity
import com.marina.ruiz.globetrotting.data.local.booking.BookingDao
import com.marina.ruiz.globetrotting.data.local.booking.BookingEntity
import com.marina.ruiz.globetrotting.data.local.destination.DestinationDao
import com.marina.ruiz.globetrotting.data.local.destination.DestinationEntity
import com.marina.ruiz.globetrotting.data.local.favorite.FavoriteDao
import com.marina.ruiz.globetrotting.data.local.favorite.FavoriteEntity
import com.marina.ruiz.globetrotting.data.local.user.UserDao
import com.marina.ruiz.globetrotting.data.local.user.UserEntity

@Database(
    entities = [DestinationEntity::class, BookingEntity::class, AgentEntity::class, UserEntity::class, FavoriteEntity::class],
    version = 1
)
abstract class GlobetrottingDb : RoomDatabase() {

    companion object {
        @Volatile // Avoid concurrency issues
        private var _INSTANCE: GlobetrottingDb? = null

        fun getInstance(context: Context): GlobetrottingDb {
            return _INSTANCE ?: synchronized(this) {
                _INSTANCE ?: buildDatabase(context).also { db -> _INSTANCE = db }
            }
        }

        private fun buildDatabase(context: Context): GlobetrottingDb {
            return Room.databaseBuilder(
                context.applicationContext, // context
                GlobetrottingDb::class.java, // db
                "globetrotting_db" // db name
            ).fallbackToDestructiveMigration()
                .build()
        }
    }

    abstract fun destinationDao(): DestinationDao
    abstract fun bookingDao(): BookingDao
    abstract fun userDao(): UserDao
    abstract fun agentDao(): AgentDao
    abstract fun favoriteDao(): FavoriteDao
}