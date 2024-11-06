package com.example.togetherpet.data.repository

import android.location.Location
import com.example.togetherpet.data.datasource.WalkingNetworkSource
import com.example.togetherpet.data.dto.LocationDTO
import com.example.togetherpet.data.dto.WalkingRequestDTO
import com.example.togetherpet.data.dto.WalkingResponseDTO
import com.kakao.vectormap.LatLng
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalkingRepository @Inject constructor(
    private val walkingNetworkSource: WalkingNetworkSource,
    private val tokenRepository: TokenRepository
) {
    suspend fun sendWalkingDataToServer(
        distance : Int,
        time : Long,
        arrayLocation : ArrayList<LatLng>
    ) {
        val locationList = arrayLocation.map { latLng ->
            LocationDTO(latLng.latitude, latLng.longitude)
        }
        walkingNetworkSource.postWalkingData(
            tokenRepository.getTokenOrThrow(),
            WalkingRequestDTO(distance.toFloat(), time, locationList)
        )
    }

    suspend fun getWalkingDataWithDateFromServer(
        date: LocalDateTime
    ) : List<WalkingResponseDTO>?{
        val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        return walkingNetworkSource.getWalkingDataWithDate( tokenRepository.getTokenOrThrow(), formattedDate)
    }
}