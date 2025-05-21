package ru.itis.androidhomework.domain.model

import ru.itis.androidhomework.domain.utils.Constants


data class FeatureDetailsModel (
    val name: String,
    val address: String,
    val rate: String,
    val wikipedia: String,
    val image: String,
    val text: String
) {
    companion object {
        val EMPTY = FeatureDetailsModel(
            name = Constants.EMPTY_STRING,
            address =Constants.EMPTY_STRING,
            rate = Constants.EMPTY_STRING,
            wikipedia = Constants.EMPTY_STRING,
            image = Constants.EMPTY_STRING,
            text = Constants.EMPTY_STRING
        )
    }
}