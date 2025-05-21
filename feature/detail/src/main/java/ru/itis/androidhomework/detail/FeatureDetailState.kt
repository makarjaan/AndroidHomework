package ru.itis.androidhomework.detail

import ru.itis.androidhomework.domain.utils.Constants

data class FeatureDetailState (
    val image: String = Constants.EMPTY_STRING,
    val placeName: String = Constants.EMPTY_STRING,
    val placeAddress: String = Constants.EMPTY_STRING,
    val desc: String = Constants.EMPTY_STRING,
    val wikilink: String = Constants.EMPTY_STRING
)

sealed interface DetailEffect {
    data class ShowError(val throwable: Throwable) : DetailEffect
}
