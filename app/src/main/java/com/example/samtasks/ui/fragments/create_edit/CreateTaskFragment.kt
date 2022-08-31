package com.example.samtasks.ui.fragments.create_edit

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.samtasks.R
import com.example.samtasks.databinding.CreateTaskFragmentBinding
import com.example.samtasks.ui.fragments.bottom_sheets.MapBottomSheet
import com.example.samtasks.ui.fragments.datepicker.DatePickerFragment
import com.example.samtasks.ui.fragments.timepicker.TimePickerFragment
import com.example.samtasks.utils.animateIntoScreen
import com.example.samtasks.utils.checkBackgroundLocationAccess
import com.example.samtasks.utils.checkLocationPermission
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class CreateTaskFragment : Fragment() {

    private val createViewModel: CreateTaskViewModel by viewModels()

    private val requestPermissionHandler =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { handleLocationPermissionResult(it) }

    private val backgroundPermissionHandler =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { handleBackgroundLocationPermissionRequest(it) }

    private lateinit var binding: CreateTaskFragmentBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CreateTaskFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        binding.apply {
            lifecycleOwner = this@CreateTaskFragment
            viewModel = createViewModel
            fragment = this@CreateTaskFragment
        }
        observe()
    }

    override fun onStart() {
        super.onStart()
        animateFabButtonOnStart()
    }

    fun showDatePicker() {
        if (!ensureNoLocationIsSet()) {
            return
        }
        val datePicker = DatePickerFragment()
        datePicker.setCallback { createViewModel.setDate(it) }
        datePicker.show(childFragmentManager, "datePicker")
    }

    @SuppressLint("InlinedApi")
    fun showMapPicker() {
        if (!checkLocationPermission(requireContext())) {
            requestPermissionHandler.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            )
            return
        }
        if (!checkBackgroundLocationAccess(requireContext())) {
            backgroundPermissionHandler.launch(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
            return
        }
        if (createViewModel.isDateOrTimeSet) {
            // We can't set location for a task that has date and time.
            Toast.makeText(context, R.string.prevent_set_location, Toast.LENGTH_SHORT).show()
            return
        }
        val mapBottomSheet = MapBottomSheet()
        mapBottomSheet.addSetLocationCallback { location: LatLng ->
            createViewModel.setTaskLocation(location)
        }
        mapBottomSheet.show(childFragmentManager, "MapBottomSheet")
    }

    fun setAlarm() {
        Toast.makeText(context, "Not Supported yet", Toast.LENGTH_LONG).show()
    }

    fun showTimePicker() {
        if (!ensureNoLocationIsSet()) {
            return
        }
        val timePicker = TimePickerFragment()
        timePicker.setCallback { createViewModel.setTime(it) }
        timePicker.show(childFragmentManager, "timePicker")
    }

    fun backWithoutSave() {
        navController.navigateUp()
    }

    /**
     * Shows a dialog to user to explain reason for needing fine location
     * permission.
     * @param goToSetting if set to true,it will navigate to app settings to edit permissions,if false it will request permission again.
     */
    private fun explainPermissionRequest(
        resId: Int,
        goToSetting: Boolean = false
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle(R.string.explain_permission_title)
            .setMessage(resId)
            .setNegativeButton(R.string.explain_permission_refuse, null)
        if (goToSetting) {
            dialogBuilder.setPositiveButton(R.string.explain_permission_accept) { _, _ ->
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts(
                        "package",
                        context?.packageName,
                        null
                    )
                    startActivity(this)
                }
            }
        } else {
            dialogBuilder.setPositiveButton(R.string.explain_permission_accept) { _, _ ->
                showMapPicker()
            }
        }
        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun handleLocationPermissionResult(resultsMap: Map<String, Boolean>) {
        if (
            resultsMap[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
            resultsMap[Manifest.permission.ACCESS_FINE_LOCATION] == true
        ) {
            showMapPicker()
        } else {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                // User refused permission but we still can show permission
                // dialog again.
                explainPermissionRequest(R.string.explain_permission_content)
            } else {
                // Permission refused and we can't show dialog so user has
                // to navigate to settings.
                explainPermissionRequest(R.string.explain_permission_content, true)
            }
        }
    }

    private fun handleBackgroundLocationPermissionRequest(granted: Boolean) {
        if (granted) {
            showMapPicker()
        } else {
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
            ) {
                explainPermissionRequest(R.string.explain_background_request)
            } else {
                explainPermissionRequest(R.string.explain_background_request, true)
            }
        }
    }

    /**
     * @return Returns false if location is set.
     */
    private fun ensureNoLocationIsSet(): Boolean {
        if (createViewModel.isLocationSet) {
            Toast.makeText(context, R.string.prevent_set_time_date, Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun observe() {
        createViewModel.apply {

            jobFinished.observe(viewLifecycleOwner) { finish ->
                if (finish) {
                    navController.navigateUp()
                }
            }

        }
    }

    private fun animateFabButtonOnStart() {
        lifecycleScope.launchWhenStarted {
            val enterAnimationDuration =
                resources.getInteger(R.integer.home_create_animations_duration).toLong()
            // Wait until the fragment is visible
            delay(enterAnimationDuration)
            binding.createFab.animateIntoScreen()
        }
    }
}