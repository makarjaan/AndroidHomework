package ru.itis.androidhomework.domain.model

data class NotificationData(
    val title: String?,
    val message: String?,
    val category: String,
    val payload: Map<String, String>
)
