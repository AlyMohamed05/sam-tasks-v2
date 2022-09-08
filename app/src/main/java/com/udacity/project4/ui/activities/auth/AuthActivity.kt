package com.udacity.project4.ui.activities.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.udacity.project4.R
import com.udacity.project4.ui.activities.host.HostActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthActivity: AppCompatActivity() {

    private val authViewModel by viewModel<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        navigateToHomeOnLogin()
    }

    private fun navigateToHomeOnLogin(){
        authViewModel.isSignedInStatus.observe(this){ isSignedIn ->
            if(isSignedIn){
                val intent = Intent(this,HostActivity::class.java)
                intent.apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(this)
                    finish()
                }
            }
        }
    }
}