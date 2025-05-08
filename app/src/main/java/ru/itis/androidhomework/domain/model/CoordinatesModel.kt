package ru.itis.androidhomework.domain.model

import ru.itis.androidhomework.utils.Constants

data class CoordinatesModel(
    val lat: Float,
    val lon: Float
) {
    companion object {
        val EMPTY = CoordinatesModel(
            lat = Constants.EMPTY_FLOAT_DATA,
            lon = Constants.EMPTY_FLOAT_DATA
        )
    }
}