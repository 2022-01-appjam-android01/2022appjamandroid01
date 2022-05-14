package com.example.a2022appjam01team

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a2022appjam01team.databinding.ActivityMainBinding

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