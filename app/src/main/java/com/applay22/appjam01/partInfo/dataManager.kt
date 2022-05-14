package com.applay22.appjam01.partInfo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class dataManager:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var reqBundle = intent.extras
        if (reqBundle !=null){
            handleRequest(reqBundle)
        }
    }

    private fun handleRequest(reqBundle: Bundle){
        val target_replace  = PartFields(
            oil_filter= 5000f,
            mission_oil = 80000f,
            break_oil = 50000f,
            power_oil = 50000f,
            spark_plugs = 400000f,
            timing_valt = 800000f,
            break_pad= 30000f,
            Brake_lining = 30000f,
            Clutch_Disc = 80000f,
            tier_location_change = 15000f,
            tier_change= 50000f,
            battery= 60000f,
            anti_freeze = 40000f,
            air_controller = 15000f,
            wiper= 10000f,
        )
        val preference = getSharedPreferences("usedChecker", MODE_PRIVATE)
        val editor = preference.edit()

        val fields =PartFields::class.java.declaredFields
        var readed_value:Float = 0f
        for (field in fields){
            readed_value  = preference.getFloat(field.name,0f)
        }
        val egin_info = preference.getInt("engineOil_filter", 0)

    }

}