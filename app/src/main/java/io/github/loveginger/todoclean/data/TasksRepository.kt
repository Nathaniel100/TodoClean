package io.github.loveginger.todoclean.data

import io.github.loveginger.todoclean.tasks.domain.model.Task


class TasksRepository : TasksDataSource {
  override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun saveTask(task: Task) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun completeTask(task: Task) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun completeTask(taskId: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun activateTask(task: Task) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun activateTask(taskId: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun clearCompletedTasks() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun refreshTasks() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun deleteAllTasks() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun deleteTask(taskId: String) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
}