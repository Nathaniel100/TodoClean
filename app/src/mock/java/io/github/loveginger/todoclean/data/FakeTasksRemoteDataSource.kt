package io.github.loveginger.todoclean.data

import android.support.annotation.VisibleForTesting
import io.github.loveginger.todoclean.tasks.domain.model.Task


object FakeTasksRemoteDataSource : TasksDataSource {

  private val tasksServiceData = mutableMapOf<String, Task>()

  override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
    callback.onTasksLoaded(ArrayList(tasksServiceData.values))
  }

  override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
    val task = tasksServiceData[taskId]
    callback.onTaskLoaded(task!!)
  }

  override fun saveTask(task: Task) {
    tasksServiceData[task.id] = task
  }

  override fun completeTask(task: Task) {
    val completedTask = Task(task.id, task.title, task.description, true)
    tasksServiceData[task.id] = completedTask
  }

  override fun completeTask(taskId: String) {
  }

  override fun activateTask(task: Task) {
    val activeTask = Task(task.id, task.title, task.description, false)
    tasksServiceData[task.id] = activeTask
  }

  override fun activateTask(taskId: String) {
  }

  override fun clearCompletedTasks() {
    val iterator = tasksServiceData.entries.iterator()
    while (iterator.hasNext()) {
      val entry = iterator.next()
      if (entry.value.completed) {
        iterator.remove()
      }
    }
  }

  override fun refreshTasks() {
  }

  override fun deleteAllTasks() {
    tasksServiceData.clear()
  }

  override fun deleteTask(taskId: String) {
    tasksServiceData.remove(taskId)
  }

  @VisibleForTesting
  fun addTasks(vararg tasks: Task) {
    for (task in tasks) {
      tasksServiceData[task.id] = task
    }
  }

}