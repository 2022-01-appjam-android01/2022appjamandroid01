package com.applay22.appjam01.partInfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.LinearLayoutManager
import com.applay22.appjam01.partInfo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mactivityreslauncher:ActivityResultLauncher<Intent>
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val data=arrayOf<String>("오일 필터","미션 오일","브레이크 오일","파워 오일","스파크 플러그","타이밍 벨트","브레이킹 패드","브레이크 라이닝","클러치 디스크","타이어 부분 변경","타이어","배터리","결빙 방지","에어컨 필터","와이퍼")
        var dist=arrayOf<String>("0","0","0","0","0","0","0","0","0","0","0","0","0","0","0")
        binding.rvView.adapter=MessageAdapter(data,dist)
        binding.rvView.layoutManager=LinearLayoutManager(this)
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