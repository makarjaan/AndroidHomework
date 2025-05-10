package ru.itis.androidhomework.domain.model

import ru.itis.androidhomework.domain.utils.Constants

data class FeaturesModel (
    val xid: String,
    val name: String,
    val rate: Int,
    val kinds: String
) {
    companion object {
        val EMPTY = FeaturesModel(
            xid = Constants.EMPTY_STRING,
            name = Constants.EMPTY_STRING,
            rate = -1,
            kinds = Constants.EMPTY_STRING
        )

    }
}