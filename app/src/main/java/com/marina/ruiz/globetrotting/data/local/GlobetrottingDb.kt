package com.marina.ruiz.globetrotting.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [TravelerEntity::class, DestinationEntity::class, BookingEntity::class, UserEntity::class],
    version = 1
)
abstract class GlobetrottingDb() : RoomDatabase() {

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

    abstract fun travelerDao(): TravelerDao
    abstract fun destinationDao(): DestinationDao
    abstract fun bookingDao(): BookingDao
    abstract fun userDao(): UserDao

}