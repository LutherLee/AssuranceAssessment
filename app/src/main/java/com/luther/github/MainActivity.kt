package com.luther.github

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.luther.github.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigationController()
    }

    private fun setupNavigationController() {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { controller: NavController, destination: NavDestination, arguments: Bundle? ->
            when (destination.id) {
                R.id.movieDetailsFragment -> enterFullScreenMode()
                else -> exitFullScreenMode()
            }
        }
    }

    private fun enterFullScreenMode() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    private fun exitFullScreenMode() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
    }

    fun disableUIInteraction() = window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )

    fun enableUIInteraction() = window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
}