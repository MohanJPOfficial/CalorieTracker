package com.mohanjp.tracker_data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.mohanjp.tracker_data.source.local.entity.TrackedFoodEntity
import com.mohanjp.tracker_data.source.local.entity.TrackedFoodTable
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackerDao {

    @Upsert
    suspend fun insertTrackedFood(trackedFoodEntity: TrackedFoodEntity)

    @Delete
    suspend fun deleteTrackedFood(trackedFoodEntity: TrackedFoodEntity)

    @Query(
        """
            SELECT * FROM ${TrackedFoodTable.TABLE_NAME}
            WHERE ${TrackedFoodTable.Columns.DAY_OF_MONTH} = :day AND
            ${TrackedFoodTable.Columns.MONTH} = :month AND
            ${TrackedFoodTable.Columns.YEAR} = :year
        """
    )
    fun getFoodsForDate(
        day: Int,
        month: Int,
        year: Int
    ): Flow<List<TrackedFoodEntity>>
}