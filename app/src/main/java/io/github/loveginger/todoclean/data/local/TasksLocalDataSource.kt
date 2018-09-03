package io.github.loveginger.todoclean.data.local

import io.github.loveginger.todoclean.data.TasksDataSource
import io.github.loveginger.todoclean.tasks.domain.model.Task
import io.github.loveginger.todoclean.util.AppExecutors


class TasksLocalDataSource(private val tasksDao: TasksDao, private val appExecutors: AppExecutors) :
  TasksDataSource {

  companion object {
    private var INSTANCE: TasksLocalDataSource? = null
    fun getInstance(tasksDao: TasksDao, appExecutors: AppExecutors): TasksLocalDataSource =
      INSTANCE ?: synchronized(this) {
        INSTANCE ?: TasksLocalDataSource(tasksDao, appExecutors).also { INSTANCE = it }
      }
  }


  override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
    appExecutors.diskIo.execute {
      val tasks = tasksDao.getTasks()
      appExecutors.mainThread.execute {
        if (tasks.isEmpty()) callback.onDataNotAvailable() else callback.onTasksLoaded(tasks)
      }
    }
  }

  override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
    appExecutors.diskIo.execute {
      val task = tasksDao.getTaskById(taskId)
      appExecutors.mainThread.execute {
        if (task == null) callback.onDataNotAvailable() else callback.onTaskLoaded(task)
      }
    }
  }

  override fun saveTask(task: Task) {
    appExecutors.diskIo.execute {
      tasksDao.insertTask(task)
    }
  }

  override fun completeTask(task: Task) {
    appExecutors.diskIo.execute {
      tasksDao.updateCompleted(task.id, true)
    }
  }

  override fun completeTask(taskId: String) {
    // Not required for the local data source because the {@link TasksRepository} handles
    // converting from a {@code taskId} to a {@link task} using its cached data.
  }

  override fun activateTask(task: Task) {
    appExecutors.diskIo.execute {
      tasksDao.updateCompleted(task.id, false)
    }
  }

  override fun activateTask(taskId: String) {
    // Not required for the local data source because the {@link TasksRepository} handles
    // converting from a {@code taskId} to a {@link task} using its cached data.
  }

  override fun clearCompletedTasks() {
    appExecutors.diskIo.execute {
      tasksDao.deleteCompletedTasks()
    }
  }

  override fun refreshTasks() {
    // Not required because the {@link TasksRepository} handles the logic of refreshing the
    // tasks from all the available data sources.
  }

  override fun deleteAllTasks() {
    appExecutors.diskIo.execute {
      tasksDao.deleteCompletedTasks()
    }
  }

  override fun deleteTask(taskId: String) {
    appExecutors.diskIo.execute {
      tasksDao.deleteTaskById(taskId)
    }
  }
}