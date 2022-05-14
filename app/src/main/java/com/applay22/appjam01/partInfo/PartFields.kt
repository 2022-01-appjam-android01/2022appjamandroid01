package com.applay22.appjam01.partInfo

import android.util.Log
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaGetter

@kotlinx.serialization.Serializable
data class PartFields(
    var oil_filter: Float = 0f,
    var mission_oil: Float = 0f,
    var break_oil: Float = 0f,
    var power_oil: Float = 0f,
    var spark_plugs: Float = 0f,
    var timing_valt: Float = 0f,
    var break_pad: Float = 0f,
    var Brake_lining: Float = 0f,
    var Clutch_Disc: Float = 0f,
    var tier_location_change: Float = 0f,
    var tier_change: Float = 0f,
    var battery: Float = 0f,
    var anti_freeze: Float = 0f,
    var air_controller: Float = 0f,
    var wiper: Float = 0f,

    ) {

    fun add_used(distance: Float) {
        var value = 0f
        this::class.memberProperties.forEach {
            if (it is KMutableProperty<*>) {
                value = it.getter.call(this) as Float
                if (value < distance) value -= distance
                it.setter.call(this, value)
            }
        }
    }
}