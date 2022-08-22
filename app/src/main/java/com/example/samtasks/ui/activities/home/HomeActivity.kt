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
import java.lang.IllegalStateException

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

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
        continueOrAuthenticate()
        if (needAuthentication) {
            // No need to show content, user will be navigated to Authentication Activity.
            return
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
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
        Timber.d("stayHome : $stayInHome")
        Timber.d("need authentication : $needAuthentication")
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