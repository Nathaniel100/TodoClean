package io.github.loveginger.todoclean.tasks.domain.model


data class Task(
  val id: String,
  val title: String,
  val description: String,
  val completed: Boolean
) {
  val active: Boolean
    get() = !completed
}