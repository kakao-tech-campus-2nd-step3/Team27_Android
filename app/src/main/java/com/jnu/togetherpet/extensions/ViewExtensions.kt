package com.jnu.togetherpet.extensions

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View

fun View.toBitmap(): Bitmap {
    this.measure(
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    this.layout(0, 0, this.measuredWidth, this.measuredHeight)
    val bitmap = Bitmap.createBitmap(this.measuredWidth, this.measuredHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}