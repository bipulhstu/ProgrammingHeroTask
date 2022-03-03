package com.bipulhstu.programminghero.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bipulhstu.programminghero.R
import com.bipulhstu.programminghero.databinding.ActivityMainBinding
import com.bipulhstu.programminghero.databinding.ActivityQuestionsBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.startButton.setOnClickListener {
            val intent = Intent(this@MainActivity, QuestionsActivity::class.java)
            startActivity(intent)
        }
    }
}