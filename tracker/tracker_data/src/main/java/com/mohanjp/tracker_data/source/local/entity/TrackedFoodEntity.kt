package com.mohanjp.tracker_data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = TrackedFoodTable.TABLE_NAME
)
data class TrackedFoodEntity(
    @ColumnInfo(name = TrackedFoodTable.Columns.NAME)
    val name: String,

    @ColumnInfo(name = TrackedFoodTable.Columns.CARBS)
    val carbs: Int,

    @ColumnInfo(name = TrackedFoodTable.Columns.PROTEIN)
    val protein: Int,

    @ColumnInfo(name = TrackedFoodTable.Columns.FAT)
    val fat: Int,

    @ColumnInfo(name = TrackedFoodTable.Columns.IMAGE_URL)
    val imageUrl: String?,

    @ColumnInfo(name = TrackedFoodTable.Columns.TYPE)
    val type: String,

    @ColumnInfo(name = TrackedFoodTable.Columns.AMOUNT)
    val amount: Int,

    @ColumnInfo(name = TrackedFoodTable.Columns.DAY_OF_MONTH)
    val dayOfMonth: Int,

    @ColumnInfo(name = TrackedFoodTable.Columns.MONTH)
    val month: Int,

    @ColumnInfo(name = TrackedFoodTable.Columns.YEAR)
    val year: Int,

    @ColumnInfo(name = TrackedFoodTable.Columns.CALORIES)
    val calories: Int,

    @ColumnInfo(name = TrackedFoodTable.Columns.ID)
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)

object TrackedFoodTable {
    const val TABLE_NAME = "tracked_food"

    object Columns {
        const val NAME = "name"
        const val CARBS = "carbs"
        const val PROTEIN = "protein"
        const val FAT = "fat"
        const val IMAGE_URL = "image_url"
        const val TYPE = "type"
        const val AMOUNT = "amount"
        const val DAY_OF_MONTH = "day_of_month"
        const val MONTH = "month"
        const val YEAR = "year"
        const val CALORIES = "calories"
        const val ID = "id"
    }
}