package ru.itis.androidhomework.data.remote.mapper

import ru.itis.androidhomework.R
import ru.itis.androidhomework.data.remote.pojo.response.Address
import ru.itis.androidhomework.data.remote.pojo.response.CoordinatesResponse
import ru.itis.androidhomework.data.remote.pojo.response.FeatureDetailsResponse
import ru.itis.androidhomework.data.remote.pojo.response.FeaturesResponse
import ru.itis.androidhomework.data.remote.pojo.response.PlacePropertiesResponse
import ru.itis.androidhomework.domain.model.CoordinatesModel
import ru.itis.androidhomework.domain.model.FeatureDetailsModel
import ru.itis.androidhomework.domain.model.FeaturesModel
import ru.itis.androidhomework.utils.Constants
import ru.itis.androidhomework.utils.ResManager
import javax.inject.Inject

class ApiResponseMapper @Inject constructor(
    private val resManager: ResManager
){

    fun mapToCoordinates(input: CoordinatesResponse?) : CoordinatesModel {
        return input?.let {
            CoordinatesModel(
                lat = it.lat ?: Constants.EMPTY_FLOAT_DATA,
                lon = it.lon ?: Constants.EMPTY_FLOAT_DATA
            )
        } ?: CoordinatesModel.EMPTY
    }

    fun mapToFeatures(input: FeaturesResponse?): List<FeaturesModel>? {
        return input?.features?.map { feature ->
            mapFeatureToModel(feature.properties)
        }
    }


    private fun mapFeatureToModel(input: PlacePropertiesResponse?) : FeaturesModel {
        return input?.let {
            FeaturesModel(
                xid = it.xid ?: Constants.EMPTY_STRING,
                name = it.name ?: Constants.EMPTY_STRING,
                rate = it.rate ?: -1,
                kinds = it.kinds ?: Constants.EMPTY_STRING
            )
        } ?: FeaturesModel.EMPTY
    }

    fun mapToDetails(input: FeatureDetailsResponse?) : FeatureDetailsModel {
        return input?.let {
            FeatureDetailsModel(
                name = it.name ?: resManager.getString(R.string.non_name),
                address = mapAddressToString(address = it.address),
                rate = it.rate ?: Constants.EMPTY_STRING,
                wikipedia = it.wikipedia ?: Constants.EMPTY_STRING,
                image = it.image ?: Constants.EMPTY_STRING,
                text = it.text?.text ?: Constants.EMPTY_STRING
            )
        } ?: FeatureDetailsModel.EMPTY
    }

    private fun mapAddressToString(address: Address?) : String {
        if (address == null) return resManager.getString(R.string.non_address)
        return listOfNotNull(
            address.road,
            address.houseNumber,
            address.pedestrian,
            address.suburb,
            address.district,
            address.city,
            address.state,
            address.country
        ).joinToString(", ")
    }
}