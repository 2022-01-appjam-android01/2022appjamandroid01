package com.applay22.appjam01.partInfo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

class DataManager : AppCompatActivity() {
    private lateinit var preference: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var saved_replace: PartFields? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preference = getSharedPreferences("usedChecker", MODE_PRIVATE)
        editor = preference.edit()

        val reqBundle = intent.extras
        if (reqBundle != null) {
            handleRequest(reqBundle)
        }
    }

    private fun handleRequest(reqBundle: Bundle) {
        when (reqBundle.getString("request")) {
            "used" -> {
                val distance = reqBundle.getFloat("distance")
                usedDistance(distance)
            }
            "reset" -> {
                val resetList = reqBundle
                    .getString("reset")!!
                    .split(",")
                    .toCollection(ArrayList<String>())
                resetUsed(resetList)
            }
        }
    }

    private fun usedDistance(distance: Float) {
        val needToMaintain = ArrayList<String>()
        saved_replace = loadSaved()
        saved_replace!!.add_used(distance)
        saved_replace!!::class.memberProperties.forEach {
            if (it is KMutableProperty<*>) {
                if (it.getter.call(saved_replace) == 0f)
                    needToMaintain.add(it.name)
            }
        }
        intent = Intent()
        intent.putExtras(Bundle())
        intent.extras!!.putString("needToMaintain", needToMaintain.joinToString(","))
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun resetUsed(resetList: ArrayList<String>) {
        val targetReplace = PartFields(
            oil_filter = 5000f,
            mission_oil = 80000f,
            break_oil = 50000f,
            power_oil = 50000f,
            spark_plugs = 400000f,
            timing_valt = 800000f,
            break_pad = 30000f,
            Brake_lining = 30000f,
            Clutch_Disc = 80000f,
            tier_location_change = 15000f,
            tier_change = 50000f,
            battery = 60000f,
            anti_freeze = 40000f,
            air_controller = 15000f,
            wiper = 10000f,
        )
        val partField = loadSaved()
        var value: Float
        partField::class.members.forEach {
            if (it.name in resetList && it is KMutableProperty<*>) {
                resetList.remove(it.name)
                value = it.getter.call(targetReplace) as Float
                it.setter.call(partField, value)
                editor.putFloat(it.name, value)
            }
        }
        editor.commit()
    }

    private fun loadSaved(): PartFields {
        val partField = PartFields()
        partField::class.memberProperties.forEach {
            if (it is KMutableProperty<*>) {
                it.setter.call(partField, preference.getFloat(it.name, 0f))
                editor.putFloat(it.name, it.getter.call(partField) as Float)
            }
        }
        editor.commit()
        return partField
    }

}