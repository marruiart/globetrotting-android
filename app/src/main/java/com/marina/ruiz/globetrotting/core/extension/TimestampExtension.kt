package com.marina.ruiz.globetrotting.core.extension

import com.google.firebase.Timestamp
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun Timestamp.daysBetween(endDate: Timestamp): Int {
    val millis1 = this.toDate().time
    val millis2 = endDate.toDate().time
    val millisDiff = abs(millis2 - millis1)
    val daysDiff = TimeUnit.MILLISECONDS.toDays(millisDiff)
    return daysDiff.toInt()
}
