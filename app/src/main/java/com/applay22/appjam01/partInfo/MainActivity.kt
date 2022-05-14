package com.applay22.appjam01.partInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.applay22.appjam01.partInfo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mactivityreslauncher:ActivityResultLauncher<Intent>
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.applycheck.setOnClickListener{
            val mIntent=Intent(this,dataManager::class.java)
            var bundle=Bundle()
            var intent=Intent()
            intent.putExtras(Bundle())
            intent.extras?.putFloat("distance",binding.curkminput.toString().toFloat())
            intent.extras?.putString("request","used")
            mactivityreslauncher.launch(mIntent)
        }
    }
}