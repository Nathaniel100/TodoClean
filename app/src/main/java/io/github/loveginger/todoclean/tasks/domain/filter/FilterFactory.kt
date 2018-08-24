package io.github.loveginger.todoclean.tasks.domain.filter

import io.github.loveginger.todoclean.tasks.TasksFilterType


object FilterFactory {

  private val filters: HashMap<TasksFilterType, TaskFilter> = hashMapOf(
    TasksFilterType.ALL_TASKS to FilterAllTaskFilter(),
    TasksFilterType.ACTIVE_TASKS to ActiveTaskFilter(),
    TasksFilterType.COMPLETED_TASKS to CompleteTaskFilter()
  )

  fun create(tasksFilterType: TasksFilterType): TaskFilter {
    return filters[tasksFilterType]!!
  }
}