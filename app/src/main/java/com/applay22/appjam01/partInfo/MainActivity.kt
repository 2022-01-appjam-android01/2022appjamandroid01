package com.applay22.appjam01.partInfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.applay22.appjam01.partInfo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.applycheck.setOnClickListener{

        }
    }
}