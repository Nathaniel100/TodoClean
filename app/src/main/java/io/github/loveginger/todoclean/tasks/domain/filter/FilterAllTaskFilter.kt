package io.github.loveginger.todoclean.tasks.domain.filter

import io.github.loveginger.todoclean.tasks.domain.model.Task


class FilterAllTaskFilter : TaskFilter {
  override fun filter(tasks: List<Task>): List<Task> {
    return ArrayList(tasks)
  }
}