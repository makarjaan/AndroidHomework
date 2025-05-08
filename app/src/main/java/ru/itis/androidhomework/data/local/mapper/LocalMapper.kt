package ru.itis.androidhomework.data.local.mapper

import ru.itis.androidhomework.data.local.entities.LocalEntity
import ru.itis.androidhomework.domain.model.LocalModel
import javax.inject.Inject

class LocalMapper @Inject constructor() {

    fun mapToLocal(local: LocalModel): LocalEntity {
        return LocalEntity(
            id = local.id,
            localName = local.name
        )
    }
}