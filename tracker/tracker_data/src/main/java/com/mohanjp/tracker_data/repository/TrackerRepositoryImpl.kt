package com.mohanjp.tracker_data.repository

import com.mohanjp.tracker_data.source.local.dao.TrackerDao
import com.mohanjp.tracker_data.source.local.entity.TrackedFoodEntity
import com.mohanjp.tracker_data.source.mapper.toTrackableFood
import com.mohanjp.tracker_data.source.mapper.toTrackedFood
import com.mohanjp.tracker_data.source.mapper.toTrackedFoodEntity
import com.mohanjp.tracker_data.source.remote.api.OpenFoodApi
import com.mohanjp.tracker_data.source.remote.dto.ProductDto
import com.mohanjp.tracker_domain.model.TrackableFood
import com.mohanjp.tracker_domain.model.TrackedFood
import com.mohanjp.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class TrackerRepositoryImpl @Inject constructor(
    private val dao: TrackerDao,
    private val api: OpenFoodApi
): TrackerRepository {

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return try {
            val searchDto = api.searchFood(
                query = query,
                page = page,
                pageSize = pageSize
            )

            /**
             * from api some products energyKcal is 0 and calculated calories wrongly, that's why filtered...
             */
            Result.success(
                searchDto
                    .products.filter {
                        val calculatedCalories = it.nutriments.carbohydrates100g * 4f +
                                it.nutriments.proteins100g * 4f +
                                it.nutriments.fat100g * 9f

                        val lowerBound = calculatedCalories * 0.99f
                        val upperBound = calculatedCalories * 1.01f

                        it.nutriments.energyKcal100g > 0 && it.nutriments.energyKcal100g in (lowerBound..upperBound)
                    }
                    .mapNotNull(ProductDto::toTrackableFood)
            )
        } catch(e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(food.toTrackedFoodEntity())
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map(TrackedFoodEntity::toTrackedFood)
        }
    }
}