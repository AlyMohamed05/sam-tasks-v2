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
import com.example.samtasks.R
import com.example.samtasks.databinding.ActivityHomeBinding
import com.example.samtasks.ui.activities.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val homeViewModel: HomeViewModel by viewModels()

    private var shouldAuthenticate = false

    /**
     * Indicates if user can continue using home screen
     * There is only 2 cases either user is already logged in
     * or he choose to continue without login
     */
    private var canContinue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val noSplash = intent.getBooleanExtra("no_splash",false)
        if(noSplash){
            setTheme(R.style.Theme_SAMTasks)
        }else{
            installSplashScreen().apply {
                setKeepOnScreenCondition {
                    // Splash Screen should be removed only if user is signed in
                    // or he choose to continue without login
                    shouldHideSplashScreen()
                }
                setOnExitAnimationListener { splashScreen ->
                    if (shouldAuthenticate) {
                        // No Animation as user will navigate to Auth Activity,
                        // and this navigation has it's own transition.\
                        Intent(this@HomeActivity, AuthActivity::class.java).apply {
                            startActivity(this)
                            finish()
                        }
                    } else {
                        playNormalSplashHideAnimation(splashScreen)
                    }
                }
            }
        }

        continueOrAuthenticate()
        if (!canContinue) {
            // No need to set content view as user will be navigated to another activity
            return
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

    }

    /**
     * It checks if the user choose to complete without login,
     * if so it will also check if he is not logged in,
     * in case he is not logged in it will set shouldAuthenticate  to true
     * and user will be navigated to Auth scree.
     */
    private fun continueOrAuthenticate() {
        val preferences = getSharedPreferences(getString(R.string.shared_pref_name), MODE_PRIVATE)
        val continueWithoutLogin =
            preferences.getBoolean(getString(R.string.continue_without_login), false)
        if (continueWithoutLogin || homeViewModel.userIsSignedIn) {
            // Then User can continue using home screen
            canContinue = true
        } else {
            // User need to be navigated to auth screen
            shouldAuthenticate = true
        }
    }

    /**
     * This function will play hide splash screen animation if the continue and
     * there is no need for authentication, so the splash screen fades away to show
     * the home screen to user.
     */
    private fun playNormalSplashHideAnimation(splashScreenViewProvider: SplashScreenViewProvider) {
        ObjectAnimator.ofFloat(
            splashScreenViewProvider.view,
            View.ALPHA,
            1f, 0f
        ).apply {
            start()
        }.doOnEnd { splashScreenViewProvider.remove() }
    }

    private fun shouldHideSplashScreen(): Boolean{
        if(shouldAuthenticate){
            return false
        }
        if(canContinue){
            return false
        }
        return true
    }
}