package com.udacity.project4.ui.activities.host

import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityHostBinding
import com.udacity.project4.utils.createChannel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HostActivity : AppCompatActivity() {

    private val viewModel by viewModel<HostViewModel>()

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
        createNotificationChannels()
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

    private fun createNotificationChannels() {
        val notificationManager = getSystemService(NotificationManager::class.java)

        // Create Channel for Sending notification when user enter geofence area.
        val geofencingChannelId =
            getString(R.string.geofencing_notification_channel_id)
        val geofencingChannelName =
            getString(R.string.geofencing_notification_channel_name)
        val geofencingChannelDescription =
            getString(R.string.geofencing_notification_channel_description)
        notificationManager.createChannel(
            geofencingChannelId,
            geofencingChannelName,
            geofencingChannelDescription
        )
    }

}