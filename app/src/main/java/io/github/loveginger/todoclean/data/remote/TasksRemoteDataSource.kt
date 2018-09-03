package io.github.loveginger.todoclean.data.remote

import android.os.Handler
import android.os.Looper
import io.github.loveginger.todoclean.data.TasksDataSource
import io.github.loveginger.todoclean.tasks.domain.model.Task

object TasksRemoteDataSource : TasksDataSource {
  private const val SERVICE_LATENCY_IN_MILLIS = 5000L
  private val tasksServiceData = mutableMapOf<String, Task>()

  init {
    addTask("Build tower in Pisa", "Ground looks good, no foundation work required.")
    addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
  }

  private fun addTask(title: String, description: String) {
    val newTask = Task(title, description)
    tasksServiceData[newTask.id] = newTask
  }

  override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed({
      callback.onTasksLoaded(ArrayList<Task>(tasksServiceData.values))
    }, SERVICE_LATENCY_IN_MILLIS)
  }

  override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
    val handler = Handler(Looper.getMainLooper())
    handler.postDelayed({
      val task = tasksServiceData[taskId]
      if (task == null) callback.onDataNotAvailable() else callback.onTaskLoaded(task)
    }, SERVICE_LATENCY_IN_MILLIS)
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
    val activatedTask = Task(task.id, task.title, task.description)
    tasksServiceData[task.id] = activatedTask
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
}