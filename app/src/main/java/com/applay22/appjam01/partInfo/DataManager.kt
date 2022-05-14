package com.applay22.appjam01.partInfo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.floor
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

class DataManager : AppCompatActivity() {
    //region components
    private lateinit var savedReplace: PartFields
    private lateinit var itemListView: ListView
    private lateinit var selectAllBtn: Button
    private lateinit var resetSelectedBtn: Button

    //endregion
    private lateinit var fields: List<KMutableProperty<*>>
    private var allSelected = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val preference = getSharedPreferences("usedChecker", MODE_PRIVATE)
        val editor = preference.edit()
        loadSaved(preference, editor)

        val reqBundle = intent.extras
        if (reqBundle != null) {
            val handler = DataManage(preference, savedReplace, fields)
            val intent = handler.handleRequest(reqBundle)
            setResult(RESULT_OK, intent)
            finish()
        }
        setContentView(R.layout.check_state)
        bindView(editor)
    }

    private fun bindView(editor: SharedPreferences.Editor) {
        itemListView = findViewById(R.id.item_list)
        resetSelectedBtn = findViewById(R.id.reset_selected)
        selectAllBtn = findViewById(R.id.select_all)

        itemListView.adapter = MaintainAdapter(savedReplace, fields, this, editor)
        selectAllBtn.setOnClickListener {
            if (allSelected) {
                selectAllBtn.text = "선택 해제"
                allSelected = false
            } else {
                selectAllBtn.text = "전체 선택"
                allSelected = true
            }
            updateSelectedAll(allSelected)
        }
        resetSelectedBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("정말로 초기화 하시겠습니까?")
            dialogBuilder.setNegativeButton("No", null)
            dialogBuilder.setPositiveButton(
                "Yes"
            ) { _, _ ->
                resetSelectedAll(editor)
                selectAllBtn.text = "전체 선택"
                allSelected = false
            }
            dialogBuilder.create().show()
        }
    }

    private fun resetSelectedAll(editor: SharedPreferences.Editor) {
        val adapter = itemListView.adapter as MaintainAdapter
        for (i in fields.indices) {
            if (adapter.selected[i])
                adapter.reset(i)
        }
        allSelected = false
        editor.commit()
        runOnUiThread { adapter.notifyDataSetChanged() }
    }

    private fun updateSelectedAll(state: Boolean) {
        val adapter = itemListView.adapter as MaintainAdapter
        Log.d("state", state.toString())
        for (i in fields.indices) {
            adapter.selected[i] = state
        }
        runOnUiThread { adapter.notifyDataSetChanged() }
    }

    private fun loadSaved(preference: SharedPreferences, editor: SharedPreferences.Editor) {
        val partField = PartFields()
        var changed = false
        fields = PartFields::class.memberProperties.toList().filterIsInstance<KMutableProperty<*>>()
        fields.forEach {
            val loadedValue = preference.getFloat(it.name, 0f)
            Log.d("Loaded", it.name)
            it.setter.call(partField, preference.getFloat(it.name, loadedValue))
            changed = true
        }
        if (changed)
            editor.commit()
        savedReplace = partField
    }

}

class MaintainAdapter(
    private var savedReplace: PartFields,
    val fields: List<KMutableProperty<*>>,
    val context: Context,
    val editor: SharedPreferences.Editor
) :
    BaseAdapter() {
    var selected: Array<Boolean> = Array(fields.size) { false }
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

    @SuppressLint("InflateParams")
    override fun getView(p0: Int, view: View?, parent: ViewGroup?): View {
        lateinit var viewHolder: ViewHolder
        lateinit var convertedView: View
        if (view == null) {
            convertedView = inflater.inflate(R.layout.check_state_item, null)
            viewHolder =
                ViewHolder(convertedView, savedReplace, p0, this)
            convertedView.tag = viewHolder
        } else {
            convertedView = view
            viewHolder = view.tag as ViewHolder
            viewHolder.updateView(savedReplace, p0)
        }
        return convertedView
    }

    fun reset(index: Int) {
        val newValue = fields[index].getter.call(targetReplace) as Float
        fields[index].setter.call(savedReplace, newValue)
        selected[index] = false
    }
}

class ViewHolder(
    view: View,
    savedReplace: PartFields,
    private var index: Int,
    private val adapter: MaintainAdapter
) {
    private var field: KMutableProperty<*> = adapter.fields[index]
    private val titleTextView: TextView = view.findViewById(R.id.item_name)
    private val stateTextView: TextView = view.findViewById(R.id.item_state)
    private val selectCheckBox: CheckBox = view.findViewById(R.id.item_select)

    fun updateView(savedReplace: PartFields, index: Int) {
        (adapter.context as AppCompatActivity).runOnUiThread {
            this.index = index
            this.field = adapter.fields[index]
            titleTextView.text = field.name
            updateLeftState(field.getter.call(savedReplace) as Float)
            this.selectCheckBox.isChecked = adapter.selected[index]
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateLeftState(leftDistance: Float) {
        val floored: Int = floor(leftDistance).toInt()
        stateTextView.text = "$floored km"
    }

    init {
        updateView(savedReplace, index)
        selectCheckBox.setOnClickListener {
            adapter.selected[index] = !adapter.selected[index]
            (adapter.context as AppCompatActivity).runOnUiThread {
                this.selectCheckBox.isChecked = adapter.selected[index]
            }
        }
        view.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(adapter.context)
            dialogBuilder.setMessage("정말로 초기화 하시겠습니까?")
            dialogBuilder.setNegativeButton("No", null)
            dialogBuilder.setPositiveButton(
                "Yes"
            ) { _, _ ->
                reset(savedReplace)
                adapter.editor.commit()
            }
            dialogBuilder.create().show()
        }
    }

    private fun reset(savedReplace: PartFields) {
        val newValue = field.getter.call(targetReplace) as Float
        field.setter.call(savedReplace, newValue)
        (adapter.context as AppCompatActivity).runOnUiThread {
            updateLeftState(newValue)
        }
        this.adapter.editor.putFloat(field.name, newValue)
    }

}

class DataManage(
    preference: SharedPreferences,
    private var saved_replace: PartFields,
    private val fields: List<KMutableProperty<*>>
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
            if (isMaintainRequired(it))
                needToMaintain.add(it.name)
        }
        return needToMaintain
    }

    private fun getRequireMaintain(): ArrayList<String> {
        val needToMaintain = ArrayList<String>()
        fields.forEach {
            if (isMaintainRequired(it))
                needToMaintain.add(it.name)
        }
        return needToMaintain
    }

    private fun resetUsed(resetList: ArrayList<String>): ArrayList<String> {
        val needToMaintain = ArrayList<String>()
        fields.forEach {
            if (it.name in resetList) {
                resetOne(it)
                resetList.remove(it.name)
            } else {
                if (isMaintainRequired(it))
                    needToMaintain.add(it.name)
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