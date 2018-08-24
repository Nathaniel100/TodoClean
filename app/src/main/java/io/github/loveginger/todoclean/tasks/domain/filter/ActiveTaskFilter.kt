package io.github.loveginger.todoclean.tasks.domain.filter

import io.github.loveginger.todoclean.tasks.domain.model.Task


class ActiveTaskFilter : TaskFilter{
  override fun filter(tasks: List<Task>): List<Task> {
    return tasks.filter { it.active }
  }
}