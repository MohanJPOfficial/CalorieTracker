package com.mohanjp.tracker_presentation.search

import com.mohanjp.tracker_domain.model.TrackableFood

data class TrackableFoodUiState(
    val trackableFood: TrackableFood,
    val isExpanded: Boolean = false,
    val amount: String = ""
)