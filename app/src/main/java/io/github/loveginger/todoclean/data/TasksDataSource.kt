package io.github.loveginger.todoclean.data

import io.github.loveginger.todoclean.tasks.domain.model.Task

interface TasksDataSource {

  interface LoadTasksCallback {

    fun onTasksLoaded(tasks: List<Task>)

    fun onDataNotAvailable()
  }

  interface GetTaskCallback {

    fun onTaskLoaded(task: Task)

    fun onDataNotAvailable()
  }

  fun activateTask(activateTask: String)
  fun completeTask(completedTask: String)
  fun clearCompletedTasks()
  fun refreshTasks()
  fun getTasks(loadTasksCallback: LoadTasksCallback)
}