package com.example.samtasks.ui.activities.host

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.samtasks.R
import com.example.samtasks.databinding.ActivityHostBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HostActivity : AppCompatActivity() {

    private val viewModel: HostViewModel by viewModels()

    private lateinit var binding: ActivityHostBinding
    private lateinit var navController: NavController

    private var hideSplash = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !hideSplash
            }
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_host)
        navController = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment)!!
            .findNavController()
        setDestinationListener()
        checkUserState()
    }

    /**
     * This function checks if user is already signed in or it's the fist time,
     * if he's signed in or choose to continue with no login , the function will
     * just hide the splash screen and return.
     * if this is the first time for the user it will navigate to welcome screen and
     * hide the splash screen.
     */
    private fun checkUserState() {
        val continueWithoutLogin = getSharedPreferences(
            getString(R.string.shared_preferences_name), MODE_PRIVATE
        )
            .getBoolean(getString(R.string.continue_without_login_key), false)
        if (continueWithoutLogin) {
            hideSplash = true
            return
        }
        hideSplash = if (viewModel.isSignedIn) {
            true
        } else {
            // The first use to user, navigate to welcome screen
            navController.popBackStack()
            navController.navigate(R.id.welcomeFragment)
            true
        }
    }

    /**
     * Hide bottom nav bar in welcome fragment
     */
    private fun setDestinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.welcomeFragment) {
                binding.bottomNavBar.visibility = View.GONE
            } else {
                binding.bottomNavBar.visibility = View.VISIBLE
            }
        }
    }
}