package com.jnu.togetherpet.extensions

import android.graphics.Color
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun KakaoMap.drawLine(arrayList: ArrayList<LatLng>){
    val layer = this.routeLineManager?.layer
    val lineStyle = RouteLineStyle.from(16f, -2134545261)
    lineStyle.strokeColor = Color.WHITE
    lineStyle.strokeWidth = 4f
    val stylesSet = RouteLineStylesSet.from(
        RouteLineStyles.from(lineStyle)
    )
    val segment = RouteLineSegment.from(
        arrayList
    ).setStyles(stylesSet.getStyles(0))
    val options = RouteLineOptions.from(segment)
        .setStylesSet(stylesSet)

    val routeLine = layer?.addRouteLine(options)
}

fun LocalDateTime.formattingLocalDateTimeToString(): String{
    val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    return this.format(formatter)
}