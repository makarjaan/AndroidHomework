package ru.itis.androidhomework.data.local.mapper

import ru.itis.androidhomework.data.local.entities.FeaturesEntity
import ru.itis.androidhomework.data.local.entities.UserEntity
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.domain.model.UserModel
import javax.inject.Inject

class DbMapper @Inject constructor() {

    fun mapToFeatureModel(entity: FeaturesEntity): FeaturesModel {
        return FeaturesModel(
            xid = entity.featureId,
            name = entity.name,
            rate = entity.rate,
            kinds = entity.kinds
        )
    }

    fun mapToFeatureModelToEntity(model: FeaturesModel, localId: String): FeaturesEntity {
        return FeaturesEntity(
            featureId = model.xid,
            localId = localId,
            name = model.name,
            rate = model.rate,
            kinds = model.kinds
        )
    }

    fun mapToUserModel(userEntity: UserEntity): UserModel {
        return UserModel(
            id = userEntity.id,
            userName = userEntity.userName,
            email = userEntity.email,
            password = userEntity.password
        )
    }

    fun mapToUserEntity(userModel: UserModel): UserEntity {
        return UserEntity(
            id = userModel.id,
            userName = userModel.userName,
            email = userModel.email,
            password = userModel.password
        )
    }

}