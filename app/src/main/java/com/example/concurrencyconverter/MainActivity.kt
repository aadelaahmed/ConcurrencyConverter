package com.example.concurrencyconverter

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.concurrencyconverter.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModel : CurrencyViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConvert.setOnClickListener {
            viewModel.convert(
                binding.edtAmount.text.toString(),
                binding.spFrom.selectedItem.toString(),
                binding.spTo.selectedItem.toString(),
            )
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect {event ->
                when(event){
                    is CurrencyViewModel.CurrencyEvent.SUCCESS -> {
                        binding.loadingProgressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.BLACK)
                        binding.tvResult.text = event.resultStr
                    }
                    is CurrencyViewModel.CurrencyEvent.FAILURE -> {
                        binding.loadingProgressBar.isVisible = false
                        binding.tvResult.setTextColor(Color.RED)
                        binding.tvResult.text = event.errorStr
                    }
                    is CurrencyViewModel.CurrencyEvent.LOADING -> {
                        binding.loadingProgressBar.isVisible = true
                    }
                    else -> Unit
                }
            }
        }
    }
}