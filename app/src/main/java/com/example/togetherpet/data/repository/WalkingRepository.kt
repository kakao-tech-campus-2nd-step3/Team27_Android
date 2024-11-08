package com.example.togetherpet.data.repository

import android.util.Log
import com.example.togetherpet.data.datasource.WalkingNetworkSource
import com.example.togetherpet.data.dto.LocationDTO
import com.example.togetherpet.data.dto.WalkingRequestDTO
import com.example.togetherpet.testData.entity.WalkingRecord
import com.kakao.vectormap.LatLng
import java.time.LocalDate
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
        date: LocalDate
    ) : ArrayList<WalkingRecord> {
        val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        try {
            val walkingResponseDTO = walkingNetworkSource.getWalkingDataWithDate(tokenRepository.getTokenOrThrow(), formattedDate)
            return walkingResponseDTO?.map { walkingResponseDTO ->
                val locationList : ArrayList<LatLng> =  walkingResponseDTO.locationList.map { locationDTO ->
                    LatLng.from(locationDTO.latitude, locationDTO.longitude)
                } as ArrayList<LatLng>
                WalkingRecord(
                    walkingResponseDTO.walkDistance.toLong(),
                    date,
                    walkingResponseDTO.walkTime,
                    LocalDateTime.parse(walkingResponseDTO.walkStartTimePoint),
                    LocalDateTime.parse(walkingResponseDTO.walkEndTimePoint),
                    1,
                    locationList
                )
            } as ArrayList<WalkingRecord>
        } catch (e : Exception){
            return arrayListOf()
        }
    }
}