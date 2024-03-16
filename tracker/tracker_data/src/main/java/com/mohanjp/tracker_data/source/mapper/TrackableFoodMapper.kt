package com.mohanjp.tracker_data.source.mapper

import com.mohanjp.tracker_data.source.remote.dto.ProductDto
import com.mohanjp.tracker_domain.model.TrackableFood
import kotlin.math.roundToInt

fun ProductDto.toTrackableFood(): TrackableFood? {
    val carbsPer100g = nutriments.carbohydrates100g.roundToInt()
    val proteinPer100g = nutriments.proteins100g.roundToInt()
    val fatPer100g = nutriments.fat100g.roundToInt()
    val caloriesPer100g = nutriments.energyKcal100g.roundToInt()

    return TrackableFood(
        name = productName ?: return null,
        carbsPer100g = carbsPer100g,
        proteinPer100g = proteinPer100g,
        fatPer100g = fatPer100g,
        caloriesPer100g = caloriesPer100g,
        imageUrl = imageFrontThumbUrl
    )
}