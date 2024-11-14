package com.jnu.togetherpet.data.dto

import com.google.gson.annotations.SerializedName

/*24.11.04 오류 발생
    Resolved [org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error:
    Cannot deserialize value of type `java.time.LocalDateTime` from Object value (token `JsonToken.START_OBJECT`)]
    -> LocalDateTime에서 String으로 변경*/
data class ReportCreateRequestDTO(
    @SerializedName("color") val color: String,
    @SerializedName("found_latitude") val foundLatitude: Double,
    @SerializedName("found_longitude") val foundLongitude: Double,
    @SerializedName("found_date") val foundDate: String, //LocalDateTIme -> String
    @SerializedName("description") val description: String,
    @SerializedName("breed") val breed: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("missing_id") val missingId: Long?
)
