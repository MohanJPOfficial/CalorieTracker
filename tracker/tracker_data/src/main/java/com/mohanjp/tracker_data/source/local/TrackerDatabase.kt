package com.mohanjp.tracker_data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mohanjp.tracker_data.source.local.dao.TrackerDao
import com.mohanjp.tracker_data.source.local.entity.TrackedFoodEntity

@Database(
    entities = [TrackedFoodEntity::class],
    version = 1
)
abstract class TrackerDatabase: RoomDatabase() {

    abstract val dao: TrackerDao

    companion object {
        const val DB_NAME = "tracker_db"
    }
}