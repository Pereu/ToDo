package com.example.mytodo.tasks

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.JOB_SCHEDULER_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.example.mytodo.EventObserver
import com.example.mytodo.R
import com.example.mytodo.data.Task
import com.example.mytodo.databinding.FragmentTasksBinding
import com.example.mytodo.service.AlarmNotificationReceiver
import com.example.mytodo.service.TaskJobService
import com.example.mytodo.util.getViewModelFactory
import com.example.mytodo.util.setupRefreshLayout
import com.example.mytodo.util.setupSnackbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.util.*

class TasksFragment : Fragment() {

    private val viewModel by viewModels<TasksViewModel> { getViewModelFactory() }

    private val args: TasksFragmentArgs by navArgs()

    private lateinit var viewDataBinding: FragmentTasksBinding

    private lateinit var listAdapter: TasksAdapter

    private var mScheduler: JobScheduler? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentTasksBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }

        setHasOptionsMenu(true)

        viewModel.getRandomTask()

        val sharedPref: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context).apply {
                getString("sort_tasks", null)
            }
        val filterValue = sharedPref.getString("sort_tasks", null)

        filterValue?.let {
            viewModel.setFiltering(TasksFilterType.getFilterByValue(it))
        }


        return viewDataBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_clear -> {
                viewModel.clearCompletedTasks()
                true
            }
            R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }
            R.id.menu_refresh -> {
                viewModel.loadTasks(true)
                true
            }
            R.id.menu_settings -> {
                navigateToSettings()
                true
            }
            else -> false
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_tasks, menu)
    }

    private fun showFilteringPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.menu_filter_tasks, menu)

            setOnMenuItemClickListener {
                viewModel.setFiltering(
                    when (it.itemId) {
                        R.id.active -> TasksFilterType.ACTIVE_TASKS
                        R.id.completed -> TasksFilterType.COMPLETED_TASKS
                        else -> TasksFilterType.ALL_TASKS
                    }
                )
                true
            }
            show()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
        setupRefreshLayout(viewDataBinding.refreshLayout, viewDataBinding.tasksList)
        setupNavigation()
        viewModel.randomTask.observe(this, EventObserver {
            startAlarm(it)
            //showNotification(it)
        })
        setupFab()
    }

    private fun showNotification(task: Task?) {
        val randomTask = task ?: return
        mScheduler = activity?.getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler

        val serviceName = ComponentName(
            activity?.packageName ?: "",
            TaskJobService::class.java.name
        )
        val bundle = PersistableBundle()
        bundle.putString("title", randomTask.title)
        bundle.putString("description", randomTask.description)


        val builder = JobInfo.Builder(0, serviceName)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setExtras(bundle)


        val myJobInfo = builder.build()
        mScheduler?.schedule(myJobInfo)

    }

    private fun setupNavigation() {
        viewModel.openTaskEvent.observe(this, EventObserver {
            openTaskDetails(it)
        })
        viewModel.newTaskEvent.observe(this, EventObserver {
            navigateToAddNewTask()
        })
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        arguments?.let {
            viewModel.showEditResultMessage(args.userMessage)
        }
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.add_task_fab)?.let {
            it.setOnClickListener {
                navigateToAddNewTask()
            }
        }
    }

    private fun setupListAdapter() {
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {
            listAdapter = TasksAdapter(viewModel)
            viewDataBinding.tasksList.adapter = listAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun openTaskDetails(taskId: String) {
        val action = TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(taskId)
        findNavController().navigate(action)
    }

    private fun navigateToAddNewTask() {
        val action = TasksFragmentDirections
            .actionTasksFragmentToAddEditTaskFragment(
                null,
                resources.getString(R.string.add_task)
            )
        findNavController().navigate(action)
    }

    private fun startAlarm(task: Task?) {
        val manager =
            activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val pendingIntent: PendingIntent

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 16)
            set(Calendar.MINUTE, 6)
            set(Calendar.SECOND, 10)
        }

        val myIntent = Intent(context, AlarmNotificationReceiver::class.java).apply {
            putExtra("title", task?.title)
            putExtra("description", task?.description)
        }

        pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, FLAG_UPDATE_CURRENT)

        manager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

    }

    private fun navigateToSettings() {
        val action = TasksFragmentDirections
            .actionTasksFragmentToSettingsFragment()
        findNavController().navigate(action)
    }
}