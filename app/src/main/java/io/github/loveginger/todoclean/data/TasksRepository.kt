package io.github.loveginger.todoclean.data

import io.github.loveginger.todoclean.tasks.domain.model.Task


class TasksRepository(
  private val tasksRemoteDataSource: TasksDataSource,
  private val tasksLocalDataSource: TasksDataSource
) : TasksDataSource {
  private var cachedTasks: MutableMap<String, Task>? = null
  private var cacheIsDirty: Boolean = true

  override fun getTasks(callback: TasksDataSource.LoadTasksCallback) {
    if (cachedTasks != null && !cacheIsDirty) {
      callback.onTasksLoaded(ArrayList(cachedTasks!!.values))
      return
    }

    if (cacheIsDirty) {
      getTasksFromRemoteDataSource(callback)
    } else {
      tasksLocalDataSource.getTasks(object : TasksDataSource.LoadTasksCallback {
        override fun onTasksLoaded(tasks: List<Task>) {
          refreshCache(tasks)
          callback.onTasksLoaded(tasks)
        }

        override fun onDataNotAvailable() {
          getTasksFromRemoteDataSource(callback)
        }
      })
    }
  }

  private fun getTasksFromRemoteDataSource(callback: TasksDataSource.LoadTasksCallback) {
    tasksRemoteDataSource.getTasks(object : TasksDataSource.LoadTasksCallback {
      override fun onTasksLoaded(tasks: List<Task>) {
        refreshCache(tasks)
        refreshLocalDataSource(tasks)
        callback.onTasksLoaded(tasks)
      }

      override fun onDataNotAvailable() {
        callback.onDataNotAvailable()
      }
    })
  }

  private fun refreshCache(tasks: List<Task>) {
    if (cachedTasks == null) {
      cachedTasks = mutableMapOf()
    }
    cachedTasks!!.clear()
    for (task in tasks) {
      cachedTasks!![task.id] = task
    }
    cacheIsDirty = false
  }

  private fun refreshLocalDataSource(tasks: List<Task>) {
    tasksLocalDataSource.deleteAllTasks()
    for (task in tasks) {
      tasksLocalDataSource.saveTask(task)
    }
  }

  override fun getTask(taskId: String, callback: TasksDataSource.GetTaskCallback) {
    val task = getTaskWithId(taskId)
    if (task != null) {
      callback.onTaskLoaded(task)
    }

    tasksLocalDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback {
      override fun onTaskLoaded(task: Task) {
        if (cachedTasks == null) {
          cachedTasks = mutableMapOf()
        }
        cachedTasks!![taskId] = task
        callback.onTaskLoaded(task)
      }

      override fun onDataNotAvailable() {
        tasksRemoteDataSource.getTask(taskId, object : TasksDataSource.GetTaskCallback {
          override fun onTaskLoaded(task: Task) {
            if (cachedTasks == null) {
              cachedTasks = mutableMapOf()
            }
            cachedTasks!![taskId] = task
            tasksLocalDataSource.saveTask(task)
            callback.onTaskLoaded(task)
          }

          override fun onDataNotAvailable() {
            callback.onDataNotAvailable()
          }
        })
      }
    })
  }

  override fun saveTask(task: Task) {
    tasksRemoteDataSource.saveTask(task)
    tasksLocalDataSource.saveTask(task)
    if (cachedTasks == null) {
      cachedTasks = mutableMapOf()
    }
    cachedTasks!![task.id] = task
  }

  override fun completeTask(task: Task) {
    tasksRemoteDataSource.completeTask(task)
    tasksLocalDataSource.completeTask(task)
    val completedTask = Task(task.id, task.title, task.description, true)
    if (cachedTasks == null) {
      cachedTasks = mutableMapOf()
    }
    cachedTasks!![task.id] = completedTask
  }

  override fun completeTask(taskId: String) {
    val task = getTaskWithId(taskId) ?: return
    completeTask(task)
  }

  override fun activateTask(task: Task) {
    tasksRemoteDataSource.activateTask(task)
    tasksLocalDataSource.activateTask(task)

    val activatedTask = Task(task.id, task.title, task.description)
    if (cachedTasks == null) {
      cachedTasks = mutableMapOf()
    }
    cachedTasks!![task.id] = activatedTask
  }

  override fun activateTask(taskId: String) {
    val task = getTaskWithId(taskId) ?: return
    activateTask(task)
  }

  override fun clearCompletedTasks() {
    tasksRemoteDataSource.clearCompletedTasks()
    tasksLocalDataSource.clearCompletedTasks()
    if (cachedTasks == null) {
      cachedTasks = mutableMapOf()
    }
    val iterator = cachedTasks!!.entries.iterator()
    while (iterator.hasNext()) {
      val entry = iterator.next()
      if (entry.value.completed) {
        iterator.remove()
      }
    }
  }

  override fun refreshTasks() {
    cacheIsDirty = true
  }

  override fun deleteAllTasks() {
    tasksRemoteDataSource.deleteAllTasks()
    tasksLocalDataSource.deleteAllTasks()
    if (cachedTasks == null) {
      cachedTasks = mutableMapOf()
    }
    cachedTasks!!.clear()
  }

  override fun deleteTask(taskId: String) {
    tasksRemoteDataSource.deleteTask(taskId)
    tasksLocalDataSource.deleteTask(taskId)
    if (cachedTasks == null) {
      cachedTasks = mutableMapOf()
    }
    cachedTasks!!.remove(taskId)
  }

  private fun getTaskWithId(taskId: String): Task? {
    return if (cachedTasks == null) null else cachedTasks!![taskId]
  }

}