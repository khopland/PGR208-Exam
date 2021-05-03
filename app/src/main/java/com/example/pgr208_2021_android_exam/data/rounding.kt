package com.example.pgr208_2021_android_exam.data

import kotlin.math.round

/**
 * Round to closest with two decimals
 */
fun rounding(d: Double): Double {
    return round((d * 100)) / 100
}