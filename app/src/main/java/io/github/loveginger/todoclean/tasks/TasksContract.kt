package io.github.loveginger.todoclean.tasks

import io.github.loveginger.todoclean.BasePresenter
import io.github.loveginger.todoclean.BaseView
import io.github.loveginger.todoclean.tasks.domain.model.Task


interface TasksContract {

  interface View : BaseView<Presenter> {

    fun setLoadingIndicator(active: Boolean)

    fun showTasks(tasks: List<Task>)

    fun showAddTask()

    fun showTaskDetaillsUi(taskId: String)

    fun showTaskMarkedComplete()

    fun showTaskMarkedActive()

    fun showCompletedTaskCleared()

    fun showLoadingTasksError()

    fun showNoTasks()

    fun showActiveFilterLabel()

    fun showCompletedFilterLabel()

    fun showAllFilterLabel()

    fun showNoActiveTasks()

    fun showNoCompltedTasks()

    fun showSuccessfullySavedMessage()

    fun isActive(): Boolean

    fun showFilteringPopUpMenu()
  }

  interface Presenter : BasePresenter {

    fun result(requestCode: Int, resultCode: Int)

    fun loadTasks(forceUpdate: Boolean)

    fun addNewTask()

    fun openTaskDetails(requestedTask: Task)

    fun completeTask(completedTask: Task)

    fun activateTask(activeTask: Task)

    fun clearCompletedTasks()

    fun setFiltering(requestType: TasksFilterType)

    fun getFiltering(): TasksFilterType
  }
}