package com.udacity.project4.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.adapters.TasksAdapter
import com.udacity.project4.data.models.Task
import com.udacity.project4.data.models.toUser
import com.udacity.project4.databinding.HomeFragmentBinding
import com.udacity.project4.ui.fragments.dialogs.datepicker.DatePickerFragment
import com.udacity.project4.ui.fragments.dialogs.TaskDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class RemindersListFragment : Fragment() {

    private val remindersListViewModel by viewModel<RemindersListViewModel>()

    private lateinit var binding: HomeFragmentBinding
    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(
            inflater,
            container,
            false
        )
        binding.greetingText.text =
            getString(
                remindersListViewModel.greetingTextResourceId,
                FirebaseAuth.getInstance().currentUser?.toUser()?.name ?: ""
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = this@RemindersListFragment
            viewModel = remindersListViewModel
            tasksAdapter = TasksAdapter()
            tasksAdapter.setTaskItemCallback { task ->
                showTaskDialog(task)
            }
            tasksRv.adapter = tasksAdapter
        }
        observe()
        checkIntentForTask()
    }

    private fun showTaskDialog(task: Task) {
        val dialog = TaskDialog(task)
        dialog.show(childFragmentManager, "taskFragment")
    }

    private fun showDatePicker() {
        val pickerFragment = DatePickerFragment()
        // TODO : Handle the date properly
        pickerFragment.setCallback { date ->
            Timber.d(date)
        }
        pickerFragment.show(childFragmentManager, "datePicker")
    }

    private fun checkIntentForTask() {
        val taskId = requireActivity().intent.getIntExtra("taskId", -1)
        if (taskId == -1) {
            return
        }
        remindersListViewModel.showTask(taskId)
    }

    private fun observe() {
        remindersListViewModel.apply {

            currentTasksList.observe(viewLifecycleOwner) { tasks ->
                tasksAdapter.submitList(tasks)
                // show no data indicator if list is empty
                if (tasks.isEmpty()) {
                    binding.timelineIndicator.visibility = View.INVISIBLE
                    binding.noDataIndicator.visibility = View.VISIBLE
                } else {
                    binding.timelineIndicator.visibility = View.VISIBLE
                    binding.noDataIndicator.visibility = View.INVISIBLE
                }
            }

            taskFromIntent.observe(viewLifecycleOwner) { task ->
                if (task != null) {
                    showTaskDialog(task)
                    remindersListViewModel.resetTaskFromIntent()
                }
            }

            createNewTask.observe(viewLifecycleOwner) { shouldCreateNewTask ->
                if (shouldCreateNewTask) {
                    findNavController().navigate(RemindersListFragmentDirections.actionHomeFragmentToCreateTaskFragment())
                    remindersListViewModel.resetCreateNewTaskEvent()
                }
            }

            showDatePicker.observe(viewLifecycleOwner) { showPicker ->
                if (showPicker) {
                    showDatePicker()
                    remindersListViewModel.resetShowDatePickerEvent()
                }
            }

        }
    }

}