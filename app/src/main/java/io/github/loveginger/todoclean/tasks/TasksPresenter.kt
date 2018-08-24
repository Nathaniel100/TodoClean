package io.github.loveginger.todoclean.tasks

import io.github.loveginger.todoclean.UseCaseCallback
import io.github.loveginger.todoclean.UseCaseHandler
import io.github.loveginger.todoclean.tasks.domain.model.Task
import io.github.loveginger.todoclean.tasks.domain.usecase.ActivateTask
import io.github.loveginger.todoclean.tasks.domain.usecase.ClearCompleteTasks
import io.github.loveginger.todoclean.tasks.domain.usecase.CompleteTask
import io.github.loveginger.todoclean.tasks.domain.usecase.GetTasks


class TasksPresenter(
  private val useCaseHandler: UseCaseHandler,
  private val tasksView: TasksContract.View,
  private val getTasks: GetTasks,
  private val completeTask: CompleteTask,
  private val activateTask: ActivateTask,
  private val clearCompleteTasks: ClearCompleteTasks
) : TasksContract.Presenter {

  private var firstLoad = true
  private var currentFiltering = TasksFilterType.ALL_TASKS

  init {
    tasksView.setPresenter(this)
  }

  override fun start() {
    loadTasks(false)
  }

  override fun result(requestCode: Int, resultCode: Int) {
    TODO(
      "not implemented"
    ) //To change body of created functions use File | Settings | File Templates.
  }

  override fun loadTasks(forceUpdate: Boolean) {
    loadTasks(forceUpdate || firstLoad, true)
    firstLoad = false
  }

  private fun loadTasks(forceUpdate: Boolean, showLoadingUi: Boolean) {
    if (showLoadingUi) {
      tasksView.setLoadingIndicator(true)
    }

    val requestValues = GetTasks.RequestValues(forceUpdate, currentFiltering)
    useCaseHandler.execute(
      getTasks,
      requestValues,
      object : UseCaseCallback<GetTasks.ResponseValue> {
        override fun onSuccess(response: GetTasks.ResponseValue) {
          if (!tasksView.isActive()) {
            return
          }
          if (showLoadingUi) {
            tasksView.setLoadingIndicator(false)
          }
          processTasks(response.tasks)
        }

        override fun onError() {
          if (!tasksView.isActive()) {
            return
          }
          tasksView.showLoadingTasksError()
        }
      })
  }

  private fun processTasks(tasks: List<Task>) {
    if (tasks.isEmpty()) {
      processEmptyTasks()
    } else {
      tasksView.showTasks(tasks)
      showFilterLabel()
    }
  }

  private fun showFilterLabel() {
    when (currentFiltering) {
      TasksFilterType.ACTIVE_TASKS -> tasksView.showActiveFilterLabel()
      TasksFilterType.COMPLETED_TASKS -> tasksView.showCompletedFilterLabel()
      else -> tasksView.showAllFilterLabel()
    }
  }

  private fun processEmptyTasks() {
    when (currentFiltering) {
      TasksFilterType.ACTIVE_TASKS -> tasksView.showNoActiveTasks()
      TasksFilterType.COMPLETED_TASKS -> tasksView.showNoCompltedTasks()
      else -> tasksView.showNoTasks()
    }
  }

  override fun addNewTask() {
    tasksView.showAddTask()
  }

  override fun openTaskDetails(requestedTask: Task) {
    tasksView.showTaskDetaillsUi(requestedTask.id)
  }

  override fun completeTask(completedTask: Task) {
    useCaseHandler.execute(
      completeTask,
      CompleteTask.RequestValues(completedTask.id),
      object : UseCaseCallback<CompleteTask.ResponseValue> {
        override fun onSuccess(response: CompleteTask.ResponseValue) {
          tasksView.showTaskMarkedComplete()
          loadTasks(false, false)
        }

        override fun onError() {
          tasksView.showLoadingTasksError()
        }
      })
  }

  override fun activateTask(activeTask: Task) {
    useCaseHandler.execute(
      activateTask,
      ActivateTask.RequestValues(activeTask.id),
      object : UseCaseCallback<ActivateTask.ResponseValue> {
        override fun onSuccess(response: ActivateTask.ResponseValue) {
          tasksView.showTaskMarkedActive()
          loadTasks(false, false)
        }

        override fun onError() {
          tasksView.showLoadingTasksError()
        }
      })
  }

  override fun clearCompletedTasks() {
    useCaseHandler.execute(
      clearCompleteTasks,
      ClearCompleteTasks.RequestValues(),
      object : UseCaseCallback<ClearCompleteTasks.ResponseValue> {
        override fun onSuccess(response: ClearCompleteTasks.ResponseValue) {
          tasksView.showCompletedTaskCleared()
          loadTasks(false, false)
        }

        override fun onError() {
          tasksView.showLoadingTasksError()
        }
      }
    )
  }

  override fun setFiltering(requestType: TasksFilterType) {
    currentFiltering = requestType
  }

  override fun getFiltering(): TasksFilterType {
    return currentFiltering
  }

}