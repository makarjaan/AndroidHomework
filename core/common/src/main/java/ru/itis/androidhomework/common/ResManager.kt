package ru.itis.androidhomework.common

import androidx.annotation.StringRes

interface ResManager {

    fun getString(@StringRes res: Int): String

    fun getString(@StringRes res: Int, vararg args: Any?): String
}