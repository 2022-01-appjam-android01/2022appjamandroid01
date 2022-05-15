package com.applay22.appjam01.partInfo

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.applay22.appjam01.partInfo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var mactivityreslauncher: ActivityResultLauncher<Intent>
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mactivityreslauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { handleResult(it) }
        binding.applycheck.setOnClickListener {
            val mIntent = Intent(this, DataManageActivity::class.java)
            val bundle = Bundle()

            val text = binding.curkminput.text.toString()
            if (text != "") {
                bundle.putFloat("distance", text.toFloat())
                bundle.putString("request", "used")
                mIntent.putExtras(bundle)
                mactivityreslauncher.launch(mIntent)
            }
            binding.curkminput.setText("")
        }
        binding.allbtn.setOnClickListener {
            val mIntent = Intent(this, DataManageActivity::class.java)
            startActivity(mIntent)
        }
        val intent = Intent(this, DataManageActivity::class.java)
        val bundle = Bundle()
        bundle.putString("request", "list")
        intent.putExtras(bundle)
        mactivityreslauncher.launch(intent)
    }

    private fun renderList(requiredList: List<String>) {
        val layoutParams = LinearLayout.LayoutParams(
            TableLayout.LayoutParams.MATCH_PARENT,
            TableLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.bottomMargin = 20
        val layout = binding.listItem
        layout.removeAllViews()
        requiredList.forEach {
            val tv = TextView(this)
            tv.textSize = 30f
            tv.layoutParams = layoutParams
            tv.text = it

            layout.addView(tv)
            tv.setOnClickListener { _ ->
                val bundle = Bundle()
                val intent = Intent(this, DataManageActivity::class.java)
                bundle.putString("request", "reset")
                bundle.putString("reset", it)
                intent.putExtras(bundle)

                mactivityreslauncher.launch(intent)
            }
        }
    }

    private fun handleResult(result: ActivityResult) {
        val bundle = result.data?.extras
        val resultTxt = bundle?.getString("needToMaintain")
        if (resultTxt != null) {
            val requiredList = resultTxt.split(",")
            runOnUiThread {
                renderList(requiredList)
            }
        }
    }
}