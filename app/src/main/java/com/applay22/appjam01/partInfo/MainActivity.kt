package com.applay22.appjam01.partInfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
            bundle.putFloat("distance", text.toFloat())
            bundle.putString("request", "used")
            mIntent.putExtras(bundle)
            mactivityreslauncher.launch(mIntent)

        }
    }

    private fun handleResult(result: ActivityResult) {
        val bundle = result.data?.extras
        val resultBundle = bundle?.getString("needToMaintain")
        if(resultBundle !=null)
            Log.d("Logggg",resultBundle)
    }


}