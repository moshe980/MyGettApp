package com.telemessage.mygettapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.telemessage.mygettapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.startTimer()

        mainViewModel.insertStubData()

        initListeners()

        initRecyclerView()

        collectFlow()

        lifecycleScope.launchWhenStarted {
            mainViewModel.searchUiState.collect {
                when (it) {
                    is MainViewModel.SearchUiState.Success -> {
                        binding.progressBar.isVisible = false
                    }
                    is MainViewModel.SearchUiState.Error -> {
                        Snackbar.make(binding.main, it.message, Snackbar.LENGTH_LONG)
                            .show()
                        binding.progressBar.isVisible = false

                    }
                    is MainViewModel.SearchUiState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    else -> {}
                }
            }
        }

        binding.searchBtn.setOnClickListener {
            searchButtonTapped()
        }


    }

    private fun initListeners() {
        binding.pickupAddressET.addTextChangedListener {
            mainViewModel.setPickupAddress(it.toString())
        }
        binding.destinationAddressET.addTextChangedListener {
            mainViewModel.setDestinationAddress(it.toString())
        }
    }

    private fun searchButtonTapped() {
        mainViewModel.searchForTaxis(
            binding.pickupAddressET.text.toString(),
            binding.destinationAddressET.text.toString(),
        )
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainViewModel.getAdapter()

        }
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            mainViewModel.isSearchEnabled.collect {
                if (!it.first) {
                    binding.pickupAddressET.error = "Invalid pickup address"
                }
                if (!it.second) {
                    binding.destinationAddressET.error = "Invalid destination address"
                }
                binding.searchBtn.isEnabled = it.first && it.second

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}