package com.applay22.appjam01.partInfo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.floor
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class DataManager : AppCompatActivity() {
    private lateinit var preference: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var savedReplace: PartFields
    private lateinit var itemList: ListView
    private lateinit var selectAll: Button
    private lateinit var resetSelected: Button
    private lateinit var fields: List<KProperty1<out PartFields, *>>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preference = getSharedPreferences("usedChecker", MODE_PRIVATE)
        editor = preference.edit()
        savedReplace = loadSaved()
        fields = savedReplace::class.memberProperties.toList()

        val reqBundle = intent.extras
        if (reqBundle != null) {
            val handler = DataManage(preference, savedReplace, fields)
            val intent = handler.handleRequest(reqBundle)
            setResult(RESULT_OK, intent)
            finish()
        }
        setContentView(R.layout.check_state)
        itemList = findViewById(R.id.item_list)
        resetSelected = findViewById(R.id.reset_selected)
        selectAll = findViewById(R.id.select_all)
        itemList.adapter = MaintainAdapter(savedReplace, this, editor)

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

class MaintainAdapter(
    private var savedReplace: PartFields,
    private val context: Context,
    private val editor: SharedPreferences.Editor
) :
    BaseAdapter() {
    private val fields = savedReplace::class.memberProperties.toList()
    private val inflater = LayoutInflater.from(context)
    override fun getCount(): Int {
        return fields.size
    }

    override fun getItem(p0: Int): Any {
        return fields[0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, parent: ViewGroup?): View {
        val view = inflater.inflate(R.layout.check_state_item, null)
        val titleTxtView: TextView = view.findViewById(R.id.item_name)
        val stateTxtView: TextView = view.findViewById(R.id.item_state)
        val field = fields[p0]
        titleTxtView.text = field.name
        if (field is KMutableProperty<*>) {
            val current = floor(field.getter.call(savedReplace) as Float).toInt()
            stateTxtView.text = "$current km"
        }
        view.setOnClickListener {
            if (field is KMutableProperty<*>) {
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setMessage("정말로 초기화 하시겠습니까?")
                dialogBuilder.setPositiveButton(
                    "Yes"
                ) { _, _ ->
                    val newValue = field.getter.call(targetReplace) as Float
                    field.setter.call(targetReplace, newValue)
                    val current = floor(newValue).toInt()
                    stateTxtView.text = "$current km"
                    editor.putFloat(field.name, newValue)
                }
                dialogBuilder.setNegativeButton("No", null).create().show()

            }
        }
        return view
    }


}


class DataManage(
    preference: SharedPreferences,
    private var saved_replace: PartFields,
    private val fields: List<KProperty1<out PartFields, *>>
) {
    private val editor: SharedPreferences.Editor = preference.edit()
    fun handleRequest(reqBundle: Bundle): Intent? {

        when (reqBundle.getString("request")) {
            "used" -> {
                val distance = reqBundle.getFloat("distance")
                val maintainList = usedDistance(distance)
                return makeMaintainListIntent(maintainList)
            }
            "reset" -> {
                val resetList = reqBundle
                    .getString("reset")!!
                    .split(",")
                    .toCollection(ArrayList())
                val maintainList = resetUsed(resetList)
                return makeMaintainListIntent(maintainList)
            }
            "list" -> {
                val maintainList = getRequireMaintain()
                return makeMaintainListIntent(maintainList)
            }
        }
        return null
    }

    private fun usedDistance(distance: Float): ArrayList<String> {
        val needToMaintain = ArrayList<String>()
        saved_replace.add_used(distance)
        fields.forEach {
            if (it is KMutableProperty<*>)
                if (isMaintainRequired(it))
                    needToMaintain.add(it.name)
        }
        return needToMaintain
    }

    private fun getRequireMaintain(): ArrayList<String> {
        val needToMaintain = ArrayList<String>()
        fields.forEach {
            if (it is KMutableProperty<*>)
                if (isMaintainRequired(it))
                    needToMaintain.add(it.name)
        }
        return needToMaintain
    }

    private fun resetUsed(resetList: ArrayList<String>): ArrayList<String> {
        val needToMaintain = ArrayList<String>()
        fields.forEach {
            if (it is KMutableProperty<*>) {
                if (it.name in resetList) {
                    resetOne(it)
                    resetList.remove(it.name)
                } else {
                    if (isMaintainRequired(it))
                        needToMaintain.add(it.name)
                }
            }
        }
        editor.commit()
        return needToMaintain
    }

    private fun resetOne(field: KMutableProperty<*>) {
        val value = field.getter.call(targetReplace) as Float
        field.setter.call(saved_replace, value)
        editor.putFloat(field.name, value)
    }

    private fun isMaintainRequired(field: KMutableProperty<*>): Boolean {
        val value = field.getter.call(saved_replace) as Float
        return value == 0f
    }

    private fun makeMaintainListIntent(needToMaintain: ArrayList<String>): Intent {
        val intent = Intent()
        intent.putExtras(Bundle())
        intent.extras!!.putString("needToMaintain", needToMaintain.joinToString(","))
        return intent
    }
}

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