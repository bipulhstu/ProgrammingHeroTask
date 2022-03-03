package com.bipulhstu.programminghero.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bipulhstu.programminghero.R
import com.bipulhstu.programminghero.databinding.ActivityMainBinding
import com.bipulhstu.programminghero.databinding.ActivityQuestionsBinding
import com.bipulhstu.programminghero.utils.SharedPreferenceInfo

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferenceInfo: SharedPreferenceInfo
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        preferenceInfo = SharedPreferenceInfo(this)

        binding.point.text = preferenceInfo.getPoint("high_score").toString()+ " Point"

        binding.startButton.setOnClickListener {
            val intent = Intent(this@MainActivity, QuestionsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.point.text = preferenceInfo.getPoint("high_score").toString()+ " Point"
    }
}