package ru.itis.androidhomework.domain.model

data class FeaturesResult(
    val features: List<FeaturesModel>,
    val source: DataSource
)

enum class DataSource {
    CACHE,
    API
}