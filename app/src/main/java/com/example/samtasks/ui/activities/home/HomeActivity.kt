package com.example.samtasks.ui.activities.home

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.samtasks.R
import com.example.samtasks.databinding.ActivityHomeBinding
import com.example.samtasks.ui.activities.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import java.lang.IllegalStateException

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var navController: NavController

    private val homeViewModel: HomeViewModel by viewModels()

    // Will be true only if user is logged in or he choose to continue
    // without login before.
    private var stayInHome = false
    private var needAuthentication = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getBooleanExtra(getString(R.string.no_splash_screen), false)) {
            setTheme(R.style.Theme_SAMTasks)
        } else {
            setupSplashScreen()
        }
        continueOrAuthenticate()
        if (needAuthentication) {
            // No need to show content, user will be navigated to Authentication Activity.
            return
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        // Setup Bottom Navigation Bar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.home_nav_host_fragment)
        navController = navHostFragment!!.findNavController()
        navController.addOnDestinationChangedListener{ _ , destination , _ ->
            if(destination.id == R.id.createTaskFragment){
                binding.bottomNavigationView.visibility = View.GONE
            }else{
                binding.bottomNavigationView.visibility = View.VISIBLE
            }
        }
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    /**
     * This function will decide if user can continue user Home screen which
     * happens if user is logged in or he choose to continue without login,
     * or The user will be navigated to Authentication activity.
     */
    private fun continueOrAuthenticate() {
        val canContinueWithoutLogin =
            getSharedPreferences(
                getString(R.string.shared_pref_name),
                MODE_PRIVATE
            ).getBoolean(getString(R.string.continue_without_login_key), false)
        if (canContinueWithoutLogin || homeViewModel.userIsSignedIn) {
            stayInHome = true
        } else {
            needAuthentication = true
        }
    }

    private fun setupSplashScreen() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                keepSplashScreen()
            }
            setOnExitAnimationListener {
                if (stayInHome) {
                    animateSplashScreen(it)
                } else if (needAuthentication) {
                    Intent(this@HomeActivity, AuthActivity::class.java).apply {
                        startActivity(this)
                    }
                    this@HomeActivity.finish()
                } else {
                    throw IllegalStateException()
                }
            }
        }
    }

    private fun keepSplashScreen(): Boolean {
        return if (needAuthentication) {
            false
        } else !stayInHome
    }

    private fun animateSplashScreen(splashScreen: SplashScreenViewProvider) {
        ObjectAnimator.ofFloat(
            splashScreen.view,
            View.ALPHA,
            1f,
            0f
        ).apply {
            duration = 500L
            start()
            doOnEnd { splashScreen.remove() }
        }
    }

}