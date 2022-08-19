package com.example.samtasks.ui.activities.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.samtasks.R
import com.example.samtasks.databinding.ActivityHomeBinding
import com.example.samtasks.ui.activities.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)

        lifecycleScope.launchWhenCreated {
            delay(1000L)
            Intent(this@HomeActivity,AuthActivity::class.java)
                .apply {
                    startActivity(this)
                }
            finish()
        }
    }
}