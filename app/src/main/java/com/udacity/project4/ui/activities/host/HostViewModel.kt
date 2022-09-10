package com.udacity.project4.ui.activities.host

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class HostViewModel(
) : ViewModel() {

    val isSignedIn = FirebaseAuth.getInstance().currentUser !=null
}