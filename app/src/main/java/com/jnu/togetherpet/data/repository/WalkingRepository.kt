package com.jnu.togetherpet.data.repository

import android.util.Log
import com.jnu.togetherpet.data.datasource.WalkingLocalSource
import com.jnu.togetherpet.data.datasource.WalkingNetworkSource
import com.jnu.togetherpet.data.dto.LocationDTO
import com.jnu.togetherpet.data.dto.WalkingRequestDTO
import com.jnu.togetherpet.data.entity.WalkEntity
import com.jnu.togetherpet.data.entity.WalkingRecord
import com.kakao.vectormap.LatLng
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WalkingRepository @Inject constructor(
    private val walkingNetworkSource: WalkingNetworkSource,
    private val tokenRepository: TokenRepository,
    private val walkingLocalSource : WalkingLocalSource
) {
    // 배포용으로 GPS 기능을 통해 얻은 위치 정보를 데이터베이스에 저장하고 활용하도록 변경
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

    suspend fun sendWalkingDataToLocal(
        distance : Int,
        walkTime : Long,
        startTime : LocalDateTime,
        endTime : LocalDateTime,
        arrayLocation : ArrayList<LatLng>,
        walkCalories : Long,
        walkDay : LocalDate
    ) {
        val walkEntity = WalkEntity(0, distance.toLong(), walkTime, startTime, endTime, arrayLocation.toList(), walkCalories, walkDay)
        walkingLocalSource.insertWalkingData(
            walkEntity
        )
    }

    suspend fun getWalkingDataWithDateFromServer(
        date: LocalDate
    ) : ArrayList<WalkingRecord> {
        try {
            val formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

            val walkingResponse = walkingNetworkSource.getWalkingDataWithDate(
                tokenRepository.getTokenOrThrow(),
                formattedDate
            )
            val walkingRecordData =  walkingResponse?.map { walkingResponseDTO ->
                val locationList: ArrayList<LatLng> =
                    walkingResponseDTO.locationList.map { locationDTO ->
                        LatLng.from(locationDTO.latitude, locationDTO.longitude)
                    } as ArrayList<LatLng>
                Log.d("testt", "locationList : $locationList")
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

            Log.d("testt", "walkingRecord : $walkingRecordData")
            return walkingRecordData?.let { ArrayList(it) } ?: arrayListOf()
        } catch (e : Exception){
            return arrayListOf()
        }
    }

    suspend fun getWalkingDataWithDateFromLocal(
        date: LocalDate
    ) : ArrayList<WalkingRecord> {
        try {
            val walkEntity = walkingLocalSource.readWalkingDataWithDate(date)
            val walkingRecordData =  walkEntity?.map { entity ->
                val locationList: ArrayList<LatLng> =
                    entity.locationList.map { location ->
                        LatLng.from(location.latitude, location.longitude)
                    } as ArrayList<LatLng>
                Log.d("testt", "locationList : $locationList")
                WalkingRecord(
                    entity.walkDistance,
                    date,
                    entity.walkTime,
                    entity.walkStartTime,
                    entity.walkEndTime,
                    entity.walkCalories,
                    locationList
                )
            }

            Log.d("testt", "walkingRecord : $walkingRecordData")
            return walkingRecordData?.let { ArrayList(it) } ?: arrayListOf()
        } catch (e : Exception){
            return arrayListOf()
        }
    }
}