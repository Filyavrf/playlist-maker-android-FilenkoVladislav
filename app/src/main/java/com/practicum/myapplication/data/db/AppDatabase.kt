package com.practicum.myapplication.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TrackEntity::class, PlaylistEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}