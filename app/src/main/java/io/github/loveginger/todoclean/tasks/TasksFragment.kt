package io.github.loveginger.todoclean.tasks

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import android.widget.BaseAdapter
import android.widget.PopupMenu
import io.github.loveginger.todoclean.R
import io.github.loveginger.todoclean.tasks.domain.model.Task
import io.github.loveginger.todoclean.util.showSnackbar
import kotlinx.android.synthetic.main.task_item.view.*
import kotlinx.android.synthetic.main.tasks_frag.*


class TasksFragment : Fragment(), TasksContract.View {

  companion object {
    fun newInstance(): TasksFragment = TasksFragment()
  }

  private lateinit var listAdapter: TasksAdapter
  private lateinit var presenter: TasksContract.Presenter
  private val itemListener: TaskItemListener = object : TaskItemListener {
    override fun onTaskClick(clickedTask: Task) {
      presenter.openTaskDetails(clickedTask)
    }

    override fun onCompleteTaskClick(completedTask: Task) {
      presenter.completeTask(completedTask)
    }

    override fun onActivateTaskClick(activatedTask: Task) {
      presenter.activateTask(activatedTask)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    listAdapter = TasksAdapter(ArrayList(), itemListener)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.tasks_frag, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    tasks_list.adapter = listAdapter
    noTasksAdd.setOnClickListener { showAddTask() }
    val fab = activity!!.findViewById<FloatingActionButton>(R.id.fab_add_task)
    fab.setImageResource(R.drawable.ic_add)
    fab.setOnClickListener { presenter.addNewTask() }
    refresh_layout.setColorSchemeColors(
      ContextCompat.getColor(activity!!, R.color.colorPrimary),
      ContextCompat.getColor(activity!!, R.color.colorAccent),
      ContextCompat.getColor(activity!!, R.color.colorPrimaryDark)
    )
    refresh_layout.scrollUpChild = tasks_list
    refresh_layout.setOnRefreshListener { presenter.loadTasks(false) }
    setHasOptionsMenu(true)
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    when (item!!.itemId) {
      R.id.menu_clear -> presenter.clearCompletedTasks()
      R.id.menu_filter -> showFilteringPopUpMenu()
      R.id.menu_refresh -> presenter.loadTasks(true)
    }
    return true
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.tasks_fragment_menu, menu)
  }

  override fun setLoadingIndicator(active: Boolean) {
    if (view == null) {
      return
    }
    // Make sure setRefreshing() is called after the layout is done with everything else.
    refresh_layout.post {
      refresh_layout.isRefreshing = active
    }
  }

  override fun showTasks(tasks: List<Task>) {
    listAdapter.replaceData(tasks)
    tasksLL.visibility = View.VISIBLE
    noTasks.visibility = View.GONE
  }

  override fun showAddTask() {
    // TODO AddEditTaskActivity
  }

  override fun showTaskDetaillsUi(taskId: String) {
    // TODO TaskDetailsActivity
  }

  override fun showTaskMarkedComplete() {
    showMessage(getString(R.string.task_marked_complete))
  }

  override fun showTaskMarkedActive() {
    showMessage(getString(R.string.task_marked_active))
  }

  override fun showCompletedTaskCleared() {
    showMessage(getString(R.string.completed_tasks_cleared))
  }

  override fun showLoadingTasksError() {
    showMessage(getString(R.string.loading_tasks_error))
  }

  override fun showNoTasks() {
    showNoTasksViews(
      resources.getString(R.string.no_tasks_all),
      R.drawable.ic_assignment_turned_in_24dp,
      false
    )
  }

  override fun showActiveFilterLabel() {
    filteringLabel.text = resources.getString(R.string.label_active)
  }

  override fun showCompletedFilterLabel() {
    filteringLabel.text = resources.getString(R.string.label_completed)
  }

  override fun showAllFilterLabel() {
    filteringLabel.text = resources.getString(R.string.label_all)
  }

  override fun showNoActiveTasks() {
    showNoTasksViews(
      resources.getString(R.string.no_tasks_active),
      R.drawable.ic_check_circle_24dp,
      false
    )
  }

  override fun showNoCompltedTasks() {
    showNoTasksViews(
      resources.getString(R.string.no_tasks_completed),
      R.drawable.ic_verified_user_24dp,
      false
    )
  }

  override fun showSuccessfullySavedMessage() {
    showMessage(getString(R.string.successfully_saved_task_message))
  }

  override fun isActive(): Boolean {
    return isAdded
  }

  override fun showFilteringPopUpMenu() {
    val popup = PopupMenu(context, activity!!.findViewById(R.id.menu_filter))
    popup.menuInflater.inflate(R.menu.filter_tasks, popup.menu)
    popup.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.active -> presenter.setFiltering(TasksFilterType.ACTIVE_TASKS)
        R.id.completed -> presenter.setFiltering(TasksFilterType.COMPLETED_TASKS)
        else -> presenter.setFiltering(TasksFilterType.ALL_TASKS)
      }
      presenter.loadTasks(false)
      true
    }
    popup.show()
  }

  override fun setPresenter(presenter: TasksContract.Presenter) {
    this.presenter = presenter
  }

  private fun showMessage(message: String) {
    showSnackbar(message)
  }

  private fun showNoTasksViews(mainText: String, iconRes: Int, showAddView: Boolean) {
    tasksLL.visibility = View.GONE
    noTasks.visibility = View.VISIBLE

    noTasksMain.text = mainText
    noTasksIcon.setImageDrawable(resources.getDrawable(iconRes))
    noTasksAdd.visibility = if (showAddView) View.VISIBLE else View.GONE
  }

  private class TasksAdapter(
    private var tasks: List<Task>,
    private val itemListener: TaskItemListener
  ) : BaseAdapter() {

    fun replaceData(tasks: List<Task>) {
      this.tasks = tasks
      notifyDataSetChanged()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
      var rowView = view
      if (rowView == null) {
        val inflater = LayoutInflater.from(parent.context)
        rowView = inflater.inflate(R.layout.task_item, parent, false)
      }
      val task = getItem(position)
      rowView!!.title.text = task.titleForList
      rowView.complete.isChecked = task.completed
      if (task.completed) {
        rowView.setBackgroundDrawable(parent.context.resources.getDrawable(R.drawable.list_completed_touch_feedback))
      } else {
        rowView.setBackgroundDrawable(parent.context.resources.getDrawable(R.drawable.touch_feedback))
      }

      rowView.complete.setOnClickListener {
        if (!task.completed) {
          itemListener.onCompleteTaskClick(task)
        } else {
          itemListener.onActivateTaskClick(task)
        }
      }

      rowView.setOnClickListener {
        itemListener.onTaskClick(task)
      }
      return rowView
    }

    override fun getItem(position: Int): Task {
      return tasks[position]
    }

    override fun getItemId(position: Int): Long {
      return position.toLong()
    }

    override fun getCount(): Int {
      return tasks.size
    }

  }

  interface TaskItemListener {
    fun onTaskClick(clickedTask: Task)

    fun onCompleteTaskClick(completedTask: Task)

    fun onActivateTaskClick(activatedTask: Task)
  }
}