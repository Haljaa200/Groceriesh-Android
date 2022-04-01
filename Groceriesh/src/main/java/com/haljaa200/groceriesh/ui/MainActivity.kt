package com.haljaa200.groceriesh.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.haljaa200.groceriesh.R
import com.haljaa200.groceriesh.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Retrofit
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var glide: RequestManager
    @Inject lateinit var retrofit: Retrofit
    lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, defaultViewModelProviderFactory).get(MainViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val mainActivityLayout = binding.root
        setContentView(mainActivityLayout)

        binding.bottomNavigationView.selectedItemId = R.id.homeFragment
        val navController: NavController = findNavController(R.id.navHostFragment)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        val navController = findNavController(R.id.navHostFragment)
        when (navController.currentDestination?.id) {
            R.id.homeFragment -> finish()
            else -> super.onBackPressed()
        }
    }
}