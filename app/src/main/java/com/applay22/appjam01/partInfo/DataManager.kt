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
    private lateinit var saved_replace: PartFields
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preference = getSharedPreferences("usedChecker", MODE_PRIVATE)
        editor = preference.edit()
        saved_replace = loadSaved()
        val reqBundle = intent.extras
        if (reqBundle != null) {
            handleRequest(reqBundle)
        }
    }

    private fun handleRequest(reqBundle: Bundle) {

        when (reqBundle.getString("request")) {
            "used" -> {
                val distance = reqBundle.getFloat("distance")
                val maintainList = usedDistance(distance)
                sendMaintainList(maintainList)
            }
            "reset" -> {
                val resetList = reqBundle
                    .getString("reset")!!
                    .split(",")
                    .toCollection(ArrayList<String>())
                val maintainList = resetUsed(resetList)
                sendMaintainList(maintainList)
            }
            "list" -> {
                val maintainList = getRequireMaintain()
                sendMaintainList(maintainList)
            }
        }

    }

    private fun getRequireMaintain(): ArrayList<String> {
        val needToMaintain = ArrayList<String>()
        saved_replace::class.members.forEach {
            if (it is KMutableProperty<*>) {
                if (it.getter.call(saved_replace) as Float == 0f) {
                    needToMaintain.add(it.name)
                }
            }
        }
        return needToMaintain
    }

    private fun sendMaintainList(needToMaintain: ArrayList<String>) {
        intent = Intent()
        intent.putExtras(Bundle())
        intent.extras!!.putString("needToMaintain", needToMaintain.joinToString(","))
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun usedDistance(distance: Float): ArrayList<String> {
        val needToMaintain = ArrayList<String>()
        saved_replace.add_used(distance)
        saved_replace::class.memberProperties.forEach {
            if (it is KMutableProperty<*>)
                if (it.getter.call(saved_replace) == 0f)
                    needToMaintain.add(it.name)
        }
        return needToMaintain
    }

    private fun resetUsed(resetList: ArrayList<String>): ArrayList<String> {
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
        var value: Float
        val needToMaintain = ArrayList<String>()
        saved_replace::class.members.forEach {
            if (it is KMutableProperty<*>) {
                if (it.name in resetList) {
                    resetList.remove(it.name)
                    value = it.getter.call(targetReplace) as Float
                    it.setter.call(saved_replace, value)
                    editor.putFloat(it.name, value)
                } else {
                    value = it.getter.call(saved_replace) as Float
                    if (value == 0f)
                        needToMaintain.add(it.name)
                }
            }
        }
        editor.commit()
        return needToMaintain
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