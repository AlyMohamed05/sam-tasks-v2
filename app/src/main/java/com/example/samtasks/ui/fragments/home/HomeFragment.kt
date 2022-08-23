package com.example.samtasks.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.samtasks.R
import com.example.samtasks.adapters.TasksAdapter
import com.example.samtasks.data.models.Task
import com.example.samtasks.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: HomeFragmentBinding
    private lateinit var tasksAdapter: TasksAdapter

    private val viewModel: HomeFragmentViewModel by viewModels()

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
        binding.greetingText.text = getString(R.string.greeting_message, viewModel.user?.name)
        binding.previousDateText.text = "31"
        binding.currentDateText.text = "1"
        binding.upcomingDateText.text = "2"
        setClickListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasksAdapter = TasksAdapter()
        binding.todoRv.apply {
            layoutManager = LinearLayoutManager(this@HomeFragment.activity)
            adapter = tasksAdapter
        }
        tasksAdapter.submitList(
            listOf(
                Task(23, "BreakFast", "Remember to have some eggs", "01:00", true),
                Task(24, "Buy Mouse", "content", "02:00", true),
                Task(25, "Fix bugs", "content", "03:00", true),
                Task(26, "IDK What", "content", "04:00", true),
                Task(27, "Whatever", "content", "05:00", true),
                Task(28, "Just another title", "content", "06:00", true),
                Task(29, "And another one", "content", "07:30", true),
                Task(30, "Go out", "content", "12:40", true),
                Task(31, "No Don't", "content", "14:00", true),
                Task(32, "Fix bugs", "content", "18:00", true),
                Task(33, "Another bug fixing", "content", "23:15", true),
                Task(34, "Nothing but bugs", "content", "23:30", true),
            )
        )
    }

    private fun setClickListeners() {
        binding.newTodoButton.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToCreateTaskFragment())
        }
    }
}