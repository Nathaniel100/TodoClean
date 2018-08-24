package io.github.loveginger.todoclean.tasks.domain.filter

import io.github.loveginger.todoclean.tasks.domain.model.Task


interface TaskFilter {
  fun filter(tasks: List<Task>): List<Task>
}