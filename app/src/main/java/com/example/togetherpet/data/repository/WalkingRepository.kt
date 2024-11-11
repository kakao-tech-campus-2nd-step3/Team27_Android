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
        try {
            val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            val walkingResponseDTO = walkingNetworkSource.getWalkingDataWithDate(
                tokenRepository.getTokenOrThrow(),
                formattedDate
            )
            val walkingRecordData =  walkingResponseDTO?.map { walkingResponseDTO ->
                val locationList: ArrayList<LatLng> =
                    walkingResponseDTO.locationList.map { locationDTO ->
                        LatLng.from(locationDTO.latitude, locationDTO.longitude)
                    } as ArrayList<LatLng>
                Log.d("testt", "locationList : ${locationList}")
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                WalkingRecord(
                    walkingResponseDTO.walkDistance.toLong(),
                    date,
                    walkingResponseDTO.walkTime,
                    LocalDateTime.parse(walkingResponseDTO.walkStartTimePoint, formatter),
                    LocalDateTime.parse(walkingResponseDTO.walkEndTimePoint, formatter),
                    1,
                    locationList
                )
            }

            Log.d("testt", "walkingRecord : ${walkingRecordData}")
            return walkingRecordData?.let { ArrayList(it) } ?: arrayListOf()
        } catch (e : Exception){
            return arrayListOf()
        }
    }
}