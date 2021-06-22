package com.pdsu.live

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.pdsu.live.databinding.ActivityMainBinding
import com.pdsu.live.viewmodel.CountDownModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val countDownModel: CountDownModel by viewModels<CountDownModel> {
        ViewModelProvider.NewInstanceFactory()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        countDownModel.countDownLivaData.observe(this,
            { value ->
                value?.let {
                    binding.tvCountdownRemainsecond.text = it
                }
            })
    }
}