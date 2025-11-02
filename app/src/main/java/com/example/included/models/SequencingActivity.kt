package com.example.included.models

import androidx.annotation.DrawableRes


data class SequenceStep(
    @get:DrawableRes val imageResId: Int,
    val description: String
)


data class SequencingActivity(
    val id: Int,
    val title: String,
    val correctSteps: List<SequenceStep>
)
